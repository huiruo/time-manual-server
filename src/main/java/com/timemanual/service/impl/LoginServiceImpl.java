package com.timemanual.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.service.LoginService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Override
    public JSONObject authLogin(JSONObject jsonObject) {
        return null;
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
