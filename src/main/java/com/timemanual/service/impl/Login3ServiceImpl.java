package com.timemanual.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.timemanual.config.jwt.JwtUtil;
import com.timemanual.config.redis.RedisUtil;
import com.timemanual.dto.SessionUserInfo;
import com.timemanual.entity.SysUser;
import com.timemanual.service.Login3Service;
import com.timemanual.service.LoginService;
import com.timemanual.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public JSONObject authLogin(String username, String password) {
        JSONObject info = new JSONObject();
        SysUser user = loginService.checkLoginUser(username);
        JSONObject userJson = loginService.checkUser(username);

        log.debug("login jwt:{}",user);
        log.debug("login2 jwt:{}",userJson);
        if(user != null){
            if (user.getPassword().equals(password)) {

                // old no currentTime
                // String generateToken = JwtUtil.sign(username, password);
                // end

                // use new JwtUtil sign
                Long currentTimeMillis = System.currentTimeMillis();
                String generateToken= JwtUtil.sign(username,currentTimeMillis);
                redisUtil.set(username,currentTimeMillis, JwtUtil.REFRESH_EXPIRE_TIME);
                // end

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
