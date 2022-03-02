package com.timemanual.config.jwt;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class JwtFilter2 extends BasicHttpAuthenticationFilter {
    /**
     * 这个方法首先执行,对跨域提供支持.
     */
    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.onPreHandle(request, response, mappedValue);
    }

    /**
     * onPreHandle方法执行后，执行这个方法，判断是否登录，返回true，允许对接口的访问，返回false，执行onAccessDenied方法。如果带有token,则对token进行检查.
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * @description: 校验token判断是否拒绝访问，校验失败返回401
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        // 从header里面获取token
        String token = req.getHeader("token");
        // 如果header里面token为空，从url里面获取token
        if (!Optional.ofNullable(token).isPresent()) {
            token = req.getParameter("token");
        }
        if (Optional.ofNullable(token).isPresent()) {
            String sessionId = JwtUtil2.getSessionId(token);
            if (!JwtUtil2.verify(token, sessionId)) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}

/*

预处理，进行验证之前执行的方法，可以理解为该过滤器最先执行的方法。该方法执行后执行isAccessAllowed方法。
1.  protected boolean onPreHandle(ServletRequest request, ServletResponse response) throws Exception，

该方法用于判断是否登录，BasicHttpAuthenticationFilter底层是通过subject.isAuthenticated()
方法判断的是否登录的。该方法返回值：如果未登录，返回false， 进入onAccessDenied。
如果登录了，返回true, 允许访问，不用继续验证，可以访问接口获取数据。
2.  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)

判断是否拒绝访问。个人理解就是当用户没有登录访问该过滤器的过滤的接口时，就必须进行httpBasic验证。
3.  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
* */