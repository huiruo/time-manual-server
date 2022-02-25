package com.timemanual.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.dao.LoginDao;
import com.timemanual.entity.SysUser;
import com.timemanual.service.LoginService;
import com.timemanual.service.PermissionService;
import com.timemanual.util.CommonUtil;
import com.timemanual.util.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
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


    /**
     * 根据用户名和密码查询对应的用户
     */
    /*
    @Override
    public JSONObject getUser(String username, String password) {
        return loginDao.getUser(username, password);
    }
    */

    /**
     * 用户名是否存在
     */
    @Override
    public JSONObject checkUser(String username) {
        return loginDao.checkUser(username);
    }

    @Override
    public SysUser checkLoginUser(String username) {
        return loginDao.checkLoginUser(username);
    }

    /**
     * 查询当前登录用户的权限等信息
     */
    @Override
    public JSONObject getInfo() {
        //从session获取用户信息
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        String username = userInfo.getString("username");
        JSONObject info = new JSONObject();
        JSONObject userPermission = permissionService.getUserPermission(username);
        session.setAttribute(Constants.SESSION_USER_PERMISSION, userPermission);
        info.put("userPermission", userPermission);
        return CommonUtil.successJson(info);
    }

    /**
     * 退出登录
     */
    @Override
    public JSONObject logout() {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();
        } catch (Exception e) {
        }
        return CommonUtil.successJson();
    }
}
