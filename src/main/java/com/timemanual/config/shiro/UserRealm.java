package com.timemanual.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.service.LoginService;
import com.timemanual.util.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/*
* 自定义Realm
* */
@Slf4j
public class UserRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private LoginService loginService;

    /*
    *  当前登录用户授权
    * */
    @Override
    @SuppressWarnings("unchecked")
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        log.debug("doGetAuthorizationInfo--->当前登录用户授权");

        Session session = SecurityUtils.getSubject().getSession();
        //查询用户的权限
        JSONObject permission = (JSONObject) session.getAttribute(Constants.SESSION_USER_PERMISSION);
        logger.info("permission的值为:" + permission);
        logger.info("本用户权限为:" + permission.get("permissionList"));
        //为当前用户设置角色和权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions((Collection<String>) permission.get("permissionList"));
        return authorizationInfo;
    }

    /** 进行用户验证
     * 验证当前登录的Subject
     * LoginController.login()方法中执行Subject.login()时 执行此方法
     * 主要内容是根据提交的用户名与密码到数据库进行匹配，如果匹配到了就返回一个AuthenticationInfo对象否则返回null，
     * 同样shiro会帮我们进行判断当返回null的时候会抛出一个异常，开发者可根据该异常进行相应的逻辑处理
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

        log.debug("doGetAuthenticationInfo--->进行用户登陆验证");

        String loginName = (String) authcToken.getPrincipal();
        // 获取用户密码
        String password = new String((char[]) authcToken.getCredentials());
        JSONObject user = loginService.getUser(loginName, password);
        if (user == null) {
            //没找到帐号
            throw new UnknownAccountException();
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getString("username"),
                user.getString("password"),
                //ByteSource.Util.bytes("salt"), salt=username+salt,采用明文访问时，不需要此句
                getName()
        );
        //session中不需要保存密码
        user.remove("password");
        //将用户信息放入session中
        SecurityUtils.getSubject().getSession().setAttribute(Constants.SESSION_USER_INFO, user);
        return authenticationInfo;
    }
}
