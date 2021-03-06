package com.timemanual.controller;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.service.Login3Service;
import com.timemanual.service.LoginService;
import com.timemanual.util.CommonUtil;
import com.timemanual.vo.ReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private Login3Service login3Service;

    /**
     * 登录
     */
    /*
    @PostMapping("/auth")
    public JSONObject authLogin(@RequestBody JSONObject requestJson) {
        System.out.println("test---->"+requestJson.toJSONString());
        CommonUtil.hasAllRequired(requestJson, "username,password");
        String username = requestJson.getString("username");
        String password = requestJson.getString("password");
        // test start
        // return login2Service.authLogin2(username,password);
        // return login3Service.authLogin3(username,password);
        return login3Service.authLogin(username,password);
        // test end
        // return loginService.authLogin(requestJson);
    }
    */

    @PostMapping("/auth")
    //public ReqVo authLogin(@RequestBody JSONObject requestJson) {
    public JSONObject authLogin(@RequestBody JSONObject requestJson) {
        System.out.println("test---->"+requestJson.toJSONString());
        CommonUtil.hasAllRequired(requestJson, "username,password");
        String username = requestJson.getString("username");
        String password = requestJson.getString("password");
        return login3Service.authLogin(username,password);
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
