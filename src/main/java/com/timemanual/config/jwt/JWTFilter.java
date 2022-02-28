package com.timemanual.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /*
     * 前置处理，我们在这进行一些跨域的必要设置
     * */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        log.debug("JWTFilter-preHandle 1---->");

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));

        // 跨域请求会发送两次请求首次为预检请求，其请求方法为 OPTIONS
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /*
     * 请求是否被允许
     * 这个方法我们会手动调用 isLoginAttempt 方法及 executeLogin 方法
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        log.debug("JWTFilter-isAccessAllowed 2---->{}",request);

        if (isLoginAttempt(request, response)) {
            try {
                log.debug("JWTFilter-isAccessAllowed 4---->{}","success");
                executeLogin(request, response);
            } catch (Exception e) {
                log.debug("JWTFilter-isAccessAllowed 5---->{}","response401");
                response401(request, response);
            }
        }
        return true;
    }

    /*
    * isLoginAttempt判断用户是否想尝试登陆，判断依据为请求头中是否包含 Authorization 授权信息，也就是 Token 令牌
    * */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {

        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");

        log.debug("JWTFilter-isLoginAttempt 2---->{}",authorization);
        return authorization != null;
    }

    /*
    * 如果有则再执行executeLogin方法进行登陆验证操作，就是我们整合后的鉴权操作，
    * 因为用Token抛开了Session,此处就相当于是否存在Session的操作，存在则表明登陆成功，
    * 不存在则需要登陆操作，或者Session过期需要重新登陆是一个原理性质，
    * 此方法在这里是验证Jwt 中Token是否合法，不合法则返回401需要重新登陆
    * */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");

        log.debug("JWTFilter-executeLogin 2---->{}",authorization);

        // 这里需要自己实现对Token验证操作
        JWTToken token = new JWTToken(authorization);
        log.debug("JWTFilter-executeLogin 3---->{}",token);
        // 如果登陆失败会抛出异常(Token鉴权失败)
         getSubject(request, response).login(token);
//        Subject currentUser = SecurityUtils.getSubject();
//        currentUser.login(token);

        log.debug("JWTFilter-executeLogin 4---->{}","成功");
        return true;
    }

    /**
     * Illege request foward to /401
     */
    private void response401(ServletRequest req, ServletResponse resp) {

        log.debug("JWTFilter-response401 1---->");
        /*
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            httpServletResponse.sendRedirect("/401");
        } catch (IOException e) {
            log.debug("JWTFilter-response401 2---->{}",e.getMessage());
        }
        */
    }
}