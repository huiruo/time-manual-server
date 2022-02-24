package com.timemanual.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.util.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/*
* 对没有登录的请求进行拦截, 全部返回json信息. 覆盖掉shiro原本的跳转login.jsp的拦截方式
* */
@Slf4j
public class AjaxPermissionsAuthorizationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ErrorEnum.E_20011.getErrorCode());
        jsonObject.put("msg", ErrorEnum.E_20011.getErrorMsg());

        log.debug("onAccessDenied 1:{}",jsonObject);

        PrintWriter out = null;

        HttpServletResponse res = (HttpServletResponse) response;
        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");

            log.debug("onAccessDenied 2");

            out = response.getWriter();
            out.println(jsonObject);
        } catch (Exception e) {
        } finally {

            log.debug("onAccessDenied 3");

            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return false;
    }

    @Bean
    public FilterRegistrationBean registration(AjaxPermissionsAuthorizationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
}
