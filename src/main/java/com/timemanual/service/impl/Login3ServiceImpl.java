package com.timemanual.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.timemanual.config.jwt.JwtUtil;
import com.timemanual.config.redis.RedisUtil;
import com.timemanual.config.shiro.ShiroAuthToken;
import com.timemanual.dto.SessionUserInfo;
import com.timemanual.entity.SysUser;
import com.timemanual.service.Login3Service;
import com.timemanual.service.LoginService;
import com.timemanual.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Login3ServiceImpl implements Login3Service {
    @Autowired
    private LoginService loginService;

    @Autowired
    Cache<String, SessionUserInfo> cacheMap;

    @Autowired
    RedisUtil redisUtil;

    // 不使用jwt,使用shiro 自带登陆
    @Override
    public JSONObject authLogin3(String username, String password) {
        JSONObject info = new JSONObject();
        Subject currentUser = SecurityUtils.getSubject();
        // UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        ShiroAuthToken token = new ShiroAuthToken(username, password);
        log.debug("authLogin3-->2,这个token包含很多用户信息:{}", token);

        try {
            currentUser.login(token);

            // String generateToken = tokenService.generateToken(username);
            // test 查询缓存信息
            // SessionUserInfo infoCacheMap = cacheMap.getIfPresent(generateToken);
            // log.debug("authLogin3-->3:{}",infoCacheMap);
            // test end

            String generateToken = JwtUtil.sign(username, password);
            Long currentTimeMillis = System.currentTimeMillis();
            redisUtil.set(username,currentTimeMillis,JwtUtil.REFRESH_EXPIRE_TIME);
            info.put("token", generateToken);
        } catch (UnknownAccountException e) {
            info.put("msg", "用户名有误");
        } catch (IncorrectCredentialsException e) {
            info.put("msg", "密码有误");
        }

        return CommonUtil.successJson(info);
    }

    @Override
    public JSONObject authLogin(String username, String password) {
        JSONObject info = new JSONObject();
        SysUser user = loginService.checkLoginUser(username);
        JSONObject userJson = loginService.checkUser(username);

        log.debug("login jwt:{}",user);
        log.debug("login2 jwt:{}",userJson);
        if(user != null){
            if (user.getPassword().equals(password)) {
                // String generateToken = JWTUtil.sign(username, password);
                String generateToken = JwtUtil.sign(username, password);
                Long currentTimeMillis = System.currentTimeMillis();
                redisUtil.set(username,currentTimeMillis,JwtUtil.REFRESH_EXPIRE_TIME);

                log.debug("取值：{}",redisUtil.get("admin"));;

                info.put("token", generateToken);
                // info.put("result", info);

                // JWTToken token = new JWTToken(generateToken);
                // Subject currentUser = SecurityUtils.getSubject();
                // currentUser.login(token);

                info.put("msg", "登录成功");

            } else {
                info.put("msg", "密码有误");
            }
        }else {
            info.put("msg", "账户不存在");
        }

        return CommonUtil.successJson(info);
    }
}
