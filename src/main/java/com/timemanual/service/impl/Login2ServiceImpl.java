package com.timemanual.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.config.exception.CommonJsonException;
import com.timemanual.dao.Login2Dao;
import com.timemanual.dto.SessionUserInfo;
import com.timemanual.service.Login2Service;
import com.timemanual.service.TokenService;
import com.timemanual.util.CommonUtil;
import com.timemanual.util.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Login2ServiceImpl implements Login2Service {

    @Autowired
    private Login2Dao login2Dao;
    @Autowired
    private TokenService tokenService;

    /**
     * 登录表单提交2
     */
    @Override
    public JSONObject authLogin2(JSONObject jsonObject) {
        System.out.println("登录表单提交2----->");
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        JSONObject info = new JSONObject();
        JSONObject user = login2Dao.checkUser(username, password);
        log.info("user----->");
        log.info("user:"+String.valueOf(user));
        if (user == null) {
            throw new CommonJsonException(ErrorEnum.E_10010);
        }
        String token = tokenService.generateToken(username);
        info.put("token", token);
        return CommonUtil.successJson(info);
    }

    /**
     * 查询当前登录用户的权限等信息
     */
    @Override
    public JSONObject getInfo() {
        //从session获取用户信息
        SessionUserInfo userInfo = tokenService.getUserInfo();
        log.info(userInfo.toString());
        return CommonUtil.successJson(userInfo);
    }

    /**
     * 退出登录
     */
    @Override
    public JSONObject logout() {
        tokenService.invalidateToken();
        return CommonUtil.successJson();
    }
}
