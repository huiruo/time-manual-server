package com.timemanual.service;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.entity.SysUser;

public interface LoginService {
    /**
     * 登录表单提交
     */
    JSONObject authLogin(JSONObject jsonObject);

    /**
     * 根据用户名和密码查询对应的用户
     */
    // JSONObject getUser(String username, String password);

//    JSONObject checkUser(String username);
    JSONObject checkUser(String username);

    SysUser checkLoginUser(String username);

    /**
     * 查询当前登录用户的权限等信息
     */
    JSONObject getInfo();

    /**
     * 退出登录
     */
    JSONObject logout();
}
