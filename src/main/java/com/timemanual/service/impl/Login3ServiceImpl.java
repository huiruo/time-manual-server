package com.timemanual.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.timemanual.config.exception.CommonJsonException;
import com.timemanual.dto.SessionUserInfo;
import com.timemanual.service.Login3Service;
import com.timemanual.service.TokenService;
import com.timemanual.util.CommonUtil;
import com.timemanual.util.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Login3ServiceImpl implements Login3Service {
    @Autowired
    private TokenService tokenService;

    @Autowired
    Cache<String, SessionUserInfo> cacheMap;


    @Override
    public JSONObject authLogin3(String username, String password) {
        JSONObject info = new JSONObject();
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            log.debug("authLogin3-->2,这个token包含很多用户信息:",token);
            currentUser.login(token);

            String generateToken = tokenService.generateToken(username);
            // test 查询缓存信息
            SessionUserInfo infoCacheMap = cacheMap.getIfPresent(generateToken);
            log.debug("authLogin3-->3:{}",infoCacheMap);
            // test end

            info.put("token", generateToken);
        } catch (UnknownAccountException e){
//            e.printStackTrace();
            info.put("msg", "用户名有误");
        }catch (IncorrectCredentialsException e){
//            e.printStackTrace();
            info.put("msg", "密码有误");
        }

        /*
        catch (AuthenticationException e) {
            System.out.println("authLogin4-->2");
            System.out.println(e);
            // info.put("result", "authLogin4-fail");
            info.put("msg", "账号/密码错误");
            //  throw new CommonJsonException(ErrorEnum.E_10010);
        }
        */
        return CommonUtil.successJson(info);
    }
}
