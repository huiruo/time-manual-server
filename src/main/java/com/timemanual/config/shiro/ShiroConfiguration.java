package com.timemanual.config.shiro;

import com.timemanual.config.exception.MultiRealmAuthenticator;
import com.timemanual.config.jwt.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.*;

@Configuration
@Slf4j
public class ShiroConfiguration {

    /**
     * Shiro Realm 继承自AuthorizingRealm的自定义Realm,即指定Shiro验证用户登录的类为自定义的
     */
    @Bean
    public UserRealm userRealm() {
        log.debug("init-userRealm----->");

        UserRealm userRealm = new UserRealm();
        return userRealm;
    }

    /**
     * 初始化Authenticator 认证器 身份认证
     */
    /*
    @Bean
    public Authenticator authenticator() {
        MultiRealmAuthenticator authenticator = new MultiRealmAuthenticator();
        // 设置两个Realm，一个用于用户登录验证和访问权限获取；一个用于jwt token的认证
        // authenticator.setRealms(Arrays.asList(jwtShiroRealm(), dbShiroRealm()));
        // 设置多个realm认证策略，一个成功即跳过其它的
        // authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }
    */

    /**
     * Shiro的Web过滤器Factory 命名:shiroFilter
     * shiro拦截器不拦截的问题:
     * 原因：shiro拦截器是基于session（会话）拦截的，如果成功登陆服务器，且不关闭浏览器的窗口，就会一直默认为登陆状态。
     * 所以给人一种没拦截的假象
     */
    // @Bean(name = "shiroFilter")
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {

        log.debug("init-shiroFilterFactoryBean----->");

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // Shiro的核心安全接口,这个属性是必须的
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = new HashMap<>(1);
        shiroFilterFactoryBean.setFilters(filterMap);
        // filterMap.put("jwt", new AjaxPermissionsAuthorizationFilter());
        // filterMap.put("authc", new AjaxPermissionsAuthorizationFilter());
        // filterMap.put("jwt", new JwtFilter());
        // test jwt
        filterMap.put("jwt", new JwtFilter());

        /*定义shiro过滤链  Map结构
         * Map中key(xml中是指value值)的第一个'/'代表的路径是相对于HttpServletRequest.getContextPath()的值来的
         * anon：它对应的过滤器里面是空的,什么都没做,这里.do和.jsp后面的*表示参数,比方说login.jsp?main这种
         * authc：该过滤器下的页面必须验证后才能访问,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        */
        /* 过滤链定义，从上向下顺序执行
          authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // filterChainDefinitionMap.put("/", "anon");
        // filterChainDefinitionMap.put("/static/**", "anon");
        // filterChainDefinitionMap.put("/login/logout", "anon");
        // filterChainDefinitionMap.put("/user/**", "authc");
        // filterChainDefinitionMap.put("/app/**", "anon");
        // filterChainDefinitionMap.put("/error", "anon");

        filterChainDefinitionMap.put("/login/auth", "anon");
        // 其余路径均拦截
        filterChainDefinitionMap.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /*
    * 自定义subject工厂
    * */
    @Bean
    public DefaultWebSubjectFactory subjectFactory() {
        return new StatelessDefaultSubjectFactory();
    }

    /*
    * 自定义session管理器
    * */
    @Bean
    public SessionManager sessionManager() {
        DefaultSessionManager shiroSessionManager = new DefaultSessionManager();
        // 关闭session校验轮询
        shiroSessionManager.setSessionValidationSchedulerEnabled(false);
        return shiroSessionManager;
    }

    /**
    * 权限管理，配置主要是Realm的管理认证
    * */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        /*
        * 关闭shiro自带的session，详情见文档
        * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
        * 博客：
        * https://blog.csdn.net/qq_39740187/article/details/119446991
        */
        // 禁用session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        // 设置自定义subject工厂
        securityManager.setSubjectFactory(subjectFactory());
        // 设置自定义session管理器
        securityManager.setSessionManager(sessionManager());

        // 设置自定义 realm.
        securityManager.setRealm(userRealm());

        return securityManager;
    }

    /**
    * 开启shiro aop注解支持. 使用代理方式; 所以需要开启代码支持;
    * @param securityManager 安全管理器
    * @return 授权Advisor
    */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        log.debug("init-authorizationAttributeSourceAdvisor----->");
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
    * 凭证匹配器
    * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
    * 所以我们需要修改下doGetAuthenticationInfo中的代码;
    * ）
    * 可以扩展凭证匹配器，实现 输入密码错误次数后锁定等功能，下一次
    */
    @Bean(name = "credentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {

        log.debug("init-hashedCredentialsMatcher----->");

        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        //storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
    * Shiro生命周期处理器
    */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
    * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
    * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
    */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
}
