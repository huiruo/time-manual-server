package com.timemanual.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Override
    public JSONObject getUserPermission(String username) {
        return null;
    }
}
