package com.timemanual.service;

import com.alibaba.fastjson.JSONObject;

public interface Login2Service {
    /**
     * 登录表单提交
     */
    JSONObject authLogin2(JSONObject jsonObject);

    /**
     * 查询当前登录用户的权限等信息
     */
    JSONObject getInfo();
    /**
     * 退出登录
     */
    JSONObject logout();
}
