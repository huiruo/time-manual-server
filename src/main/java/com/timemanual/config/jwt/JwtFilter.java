package com.timemanual.config.jwt;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.timemanual.config.redis.RedisUtil;
import com.timemanual.util.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    /*
     * 前置处理，我们在这进行一些跨域的必要设置
     * 该方法执行后执行isAccessAllowed方法
     * */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        log.debug("JWTFilter-preHandle 2---->{}",httpServletRequest.getRequestURI());

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
     * 该方法返回值：如果未登录，返回false， 进入 onAccessDenied
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        log.debug("====isAccessAllowed======");

        // 查看当前Header中是否携带Authorization属性
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
                /*
                Boolean isAccessAllowed = executeLogin(request, response);

                log.debug("JWTFilter-isAccessAllowed 5---->{}",isAccessAllowed);
                if(!isAccessAllowed){
                    onAccessDeniedCallback(request,response);
                }
                return isAccessAllowed;
                */
                return true;
            } catch (Exception e) {
                log.debug("JWTFilter-isAccessAllowed 走刷新逻辑---->:{}",e.getMessage());
                // 认证出现异常，传递错误信息msg

                String msg = e.getMessage();

                // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
                Throwable throwable = e.getCause();

                if (throwable instanceof SignatureVerificationException) {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                    log.debug("JWTFilter-isAccessAllowed 走刷新逻辑---->{}","Token或者密钥不正确");
                    return false;
                } else if (throwable instanceof TokenExpiredException) {

                    log.debug("JWTFilter-isAccessAllowed 走刷新逻辑2---->{}","该异常为JWT的AccessToken已过期");

                    if (refreshToken(request, response)) {
                        log.debug("JWTFilter-isAccessAllowed 走刷新逻辑3---->{}","成功续签");
                        return true;
                    }else {
                        log.debug("JWTFilter-isAccessAllowed 走刷新逻辑4---->{}","过期不可以续签");
                        onAccessDeniedCallback(request,response);
                        return false;
                    }
                    /*
                    // 该异常为JWT的AccessToken已过期，判断RefreshToken未过期就进行AccessToken刷新
                    if (this.refreshToken(request, response)) {
                        return true;
                    } else {
                        msg = "Token已过期(" + throwable.getMessage() + ")";
                    }
                    */
                } else {
                    log.debug("JWTFilter-isAccessAllowed 走刷新逻辑---->{}","应用异常不为空");
                    onAccessDeniedCallback(request,response);
                    return false;
                }
            }
        }else {
            log.debug("JWTFilter-isAccessAllowed---->{}","没携带token直接拦截");
            onAccessDeniedCallback(request,response);
            return false;
        }
    }

    /*
     * isLoginAttempt判断用户是否想尝试登陆，判断依据为请求头中是否包含 Authorization 授权信息，也就是 Token 令牌
     *
     * */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {

        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(JwtUtil.AUTH_HEADER_KEY);

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

        log.debug("JWTFilter-executeLogin 4---->{}",httpServletRequest.getRequestURI());

        // 这里需要自己实现对Token验证操作
        JwtToken token = new JwtToken(authorization);
        this.getSubject(request, response).login(token);

        // 如果登陆失败会抛出异常(Token鉴权失败)
        /*
        try{
            getSubject(request, response).login(token);
        }catch (Exception e){
            log.debug("JWTFilter-executeLogin 4----失败：{}",e.getMessage());
            return false;
        }
        */

        log.debug("JWTFilter-executeLogin 4---->{}","成功");
        return true;
    }

    private void onAccessDeniedCallback(ServletRequest request, ServletResponse response){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ErrorEnum.E_401.getErrorCode());
        jsonObject.put("msg", ErrorEnum.E_401.getErrorMsg());

        log.debug("onAccessDenied 1:{}",jsonObject);

        PrintWriter out = null;
        HttpServletResponse res = (HttpServletResponse) response;
        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            out = response.getWriter();
            out.println(jsonObject);
        } catch (Exception e) {
            log.debug("onAccessDenied 2:{}",e.getMessage());
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 刷新AccessToken，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     * @param request
     * @param response
     * @return
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        String token = ((HttpServletRequest)request).getHeader(JwtUtil.AUTH_HEADER_KEY);
        String account = JwtUtil.getAccount(token);
        Long currentTime = JwtUtil.getCurrentTime(token);
        // 判断Redis中RefreshToken是否存在
        if (RedisUtil.hasKey(account)) {
            // Redis中RefreshToken还存在，获取RefreshToken的时间戳
            Long currentTimeMillisRedis = (Long) RedisUtil.get(account);
            // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
            if (currentTimeMillisRedis.equals(currentTime)) {
                // 获取当前最新时间戳
                Long currentTimeMillis =System.currentTimeMillis();
                RedisUtil.set(account, currentTimeMillis,
                        JwtUtil.REFRESH_EXPIRE_TIME);
                // 刷新AccessToken，设置时间戳为当前最新时间戳
                token = JwtUtil.sign(account, currentTimeMillis);
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader("RefreshToken", token);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "RefreshToken");
                return true;
            }
        }else {
            log.debug("续签：{}","redis 账户不存在");
        }
        return false;
    }
}