package com.timemanual.config.jwt;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.util.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    @Autowired
    // private RedisUtil redisUtil;
    private AntPathMatcher antPathMatcher =new AntPathMatcher();
    /**
     * 执行登录认证(判断请求头是否带上token)
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        Boolean loginAttempt = isLoginAttempt(request, response);
        log.info("JwtFilter-->>>isAccessAllowed:{}",loginAttempt);

        // 如果请求头不存在token,则可能是执行登陆操作或是游客状态访问,直接返回true
        // 如果存在,则进入executeLogin方法执行登入,检查token 是否正确
        if (loginAttempt) {
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                log.debug("登陆：{}",e.getMessage());
                throw new AuthenticationException("Token失效请重新登录");
            }
        }else{
            PrintWriter out = null;

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", ErrorEnum.E_20011.getErrorCode());
                jsonObject.put("msg", ErrorEnum.E_20011.getErrorMsg());

                HttpServletResponse res = (HttpServletResponse) response;
                res.setCharacterEncoding("UTF-8");
                res.setContentType("application/json");

                log.debug("JwtFilter-->>>isAccessAllowed onAccessDenied 2,{}","返回值");

                out = response.getWriter();
                out.println(jsonObject);
            } catch (Exception e) {
               log.debug("JwtFilter-->>>isAccessAllowed isAccessAllowed exception:{}",e.getMessage());
                return false;
            } finally {
                if (null != out) {
                    out.flush();
                    out.close();
                }
            }
            return false;
        }
    }

    /**
     * 判断用户是否是登入,检测headers里是否包含token字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(CommonConstant.ACCESS_TOKEN);
        log.info("JwtFilter-->>>isLoginAttempt {}",req.getRequestURI());
        /*
        if(antPathMatcher.match("/userLogin",req.getRequestURI())){
            return true;
        }
        */
        log.debug("isLoginAttempt:{}",token);
        // Object o = redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token);
        // if(ObjectUtils.isEmpty(o)){
        //     return false;
        // }
        return token != null;
    }

    /**
     * 重写AuthenticatingFilter的executeLogin方法丶执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

        log.info("JwtFilter-->>>executeLogin:");

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(CommonConstant.ACCESS_TOKEN);//Access-Token

        log.debug("executeLogin: 1,{}",token);

        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入,如果错误他会抛出异常并被捕获, 反之则代表登入成功,返回true
        getSubject(request, response).login(jwtToken);

        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        log.info("JwtFilter-->>>preHandle:");

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

        return super.preHandle(request, response);
    }
}