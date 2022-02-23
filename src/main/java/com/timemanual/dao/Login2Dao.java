package com.timemanual.dao;


import com.alibaba.fastjson.JSONObject;
import com.timemanual.dto.SessionUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/*
* 登录相关dao
* */
public interface Login2Dao {
    /**
     * 根据用户名和密码查询对应的用户
     */
    JSONObject checkUser(@Param("username") String username, @Param("password") String password);

    SessionUserInfo getUserInfo(String username);

    Set<String> getAllMenu();

    Set<String> getAllPermissionCode();

}
