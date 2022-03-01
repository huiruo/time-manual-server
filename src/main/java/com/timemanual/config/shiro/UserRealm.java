package com.timemanual.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.config.jwt.JwtToken;
import com.timemanual.config.jwt.JwtUtil;
import com.timemanual.entity.SysUser;
import com.timemanual.service.LoginService;
import com.timemanual.util.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authc.AuthenticationToken;
import java.util.Collection;

@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /*
    * 授权(验证权限时调用)
    * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
    * */
    @Override
    @SuppressWarnings("unchecked")
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        log.debug("doGetAuthorizationInfo--->当前登录用户授权");

        Session session = SecurityUtils.getSubject().getSession();
        //查询用户的权限
        JSONObject permission = (JSONObject) session.getAttribute(Constants.SESSION_USER_PERMISSION);
        log.info("permission的值为:" + permission);
        log.info("本用户权限为:" + permission.get("permissionList"));
        //为当前用户设置角色和权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions((Collection<String>) permission.get("permissionList"));
        return authorizationInfo;
    }

    /**
    * 认证(登录时调用)
    * 每次请求的时候都会调用这个方法验证token是否失效和用户是否被锁定
    * 验证当前登录的Subject
    * LoginController.login()方法中执行Subject.login()时 执行此方法
    * 主要内容是根据提交的用户名与密码到数据库进行匹配，如果匹配到了就返回一个AuthenticationInfo对象否则返回null，
    * 同样shiro会帮我们进行判断当返回null的时候会抛出一个异常，开发者可根据该异常进行相应的逻辑处理
    */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) {
        log.info("====================Token认证====================");
        String token = (String) auth.getCredentials();

        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);

        log.debug("doGetAuthenticationInfo 1:{}",token);
        log.debug("doGetAuthenticationInfo 2:{}",username);

        if (username == null) {
            log.debug("doGetAuthenticationInfo 2-1:");
            throw new AuthenticationException("token invalid");
        }

        SysUser user = loginService.checkLoginUser(username);

        log.debug("doGetAuthenticationInfo 3:{}",user);

        if (user == null) {
            log.debug("doGetAuthenticationInfo 3-1:");
            throw new AuthenticationException("User didn't existed!");
        }

        if (!JwtUtil.verify(token, username, user.getPassword())) {
            log.debug("doGetAuthenticationInfo 4:{}","Username or password error");
            throw new AuthenticationException("Username or password error");
        }else {
            log.debug("doGetAuthenticationInfo 4-2:{}","success");
        }

        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
