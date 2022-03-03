package com.timemanual.service;

import com.alibaba.fastjson.JSONObject;

public interface Login3Service {

    JSONObject authLogin(String username, String password);
}
