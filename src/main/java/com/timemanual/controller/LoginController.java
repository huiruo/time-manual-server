package com.timemanual.controller;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.service.LoginService;
import com.timemanual.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 登录
     */
    @PostMapping("/auth")
    public JSONObject authLogin(@RequestBody JSONObject requestJson) {
        log.debug("router-auth",requestJson);
        CommonUtil.hasAllRequired(requestJson, "username,password");
        return loginService.authLogin(requestJson);
    }

    /**
     * 查询当前登录用户的信息
     */
    @PostMapping("/getInfo")
    public JSONObject getInfo() {
        return loginService.getInfo();
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public JSONObject logout() {
        return loginService.logout();
    }
}
