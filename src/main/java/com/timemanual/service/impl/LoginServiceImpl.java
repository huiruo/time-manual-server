package com.timemanual.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.dao.LoginDao;
import com.timemanual.service.LoginService;
import com.timemanual.service.PermissionService;
import com.timemanual.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDao loginDao;
    @Autowired
    private PermissionService permissionService;

    /*
    * 登录
    * */
    @Override
    public JSONObject authLogin(JSONObject jsonObject) {
        log.info("service authLogin-->");
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        JSONObject info = new JSONObject();
        Subject currentUser = SecurityUtils.getSubject();
        log.info(username);
        log.info(password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        System.out.println("token:"+token);
        try {
            currentUser.login(token);
            info.put("result", "success");
        } catch (AuthenticationException e) {
            info.put("result", "fail");
        }
        return CommonUtil.successJson(info);
    }

    @Override
    public JSONObject getUser(String username, String password) {
        return null;
    }

    @Override
    public JSONObject getInfo() {
        return null;
    }

    @Override
    public JSONObject logout() {
        return null;
    }
}
