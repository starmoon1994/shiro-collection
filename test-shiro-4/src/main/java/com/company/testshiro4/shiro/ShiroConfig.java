package com.company.testshiro4.shiro;


import com.company.testshiro4.shiro.mongo.ShiroMongoCacheManager;
import com.company.testshiro4.shiro.mongo.ShiroMongoSessionDao;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.*;


@Configuration
public class ShiroConfig {

    @Value("${server.session-timeout}")
    private int globalSessionTimeout;

    @Autowired
    private ShiroMongoCacheManager shiroMongoCacheManager;

    @Autowired
    private MyShiroRealm myShiroRealm;

    @Autowired
    private ShiroMongoSessionDao shiroMongoSessionDao;


    /**
     * 在此配置拦截器栈
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("init ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置不会被拦截的链接 顺序判断 注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/logout.do", "logout");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        // ajax方式的登录
        filterChainDefinitionMap.put("/ajaxLogin.do", "anon");
        // ajax方式的退出登录
        filterChainDefinitionMap.put("/ajaxLogout.do", "anon");
        filterChainDefinitionMap.put("/login.do", "anon");
        filterChainDefinitionMap.put("/**", "authc");
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/unauth.do");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauth.do");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(shiroMongoCacheManager);
        securityManager.setSessionManager(sessionManager());
        securityManager.setRealm(myShiroRealm);
        return securityManager;
    }

    /**
     * shiro session的管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // 注入自定义sessionDao操作的实现类
        sessionManager.setSessionDAO(shiroMongoSessionDao);

        // 设置Listeners
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        listeners.add(new BDSessionListener());
        sessionManager.setSessionListeners(listeners);

        // 设置安全cookie的名字和过期时间 此Cookie是shiro提供的规范
        sessionManager.setSessionIdCookieEnabled(true);
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("g_s");
        simpleCookie.setMaxAge(60 * 60 * 24 * 30);
        sessionManager.setSessionIdCookie(simpleCookie);
        sessionManager.setGlobalSessionTimeout(60 * 60 * 24 * 30 * 1000);

        return sessionManager;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {

        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();

        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);

        return authorizationAttributeSourceAdvisor;
    }


    @Bean(name = "simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
        mappings.setProperty("UnauthorizedException", "403");
        r.setExceptionMappings(mappings);  // None by default
        r.setDefaultErrorView("error");    // No default
        r.setExceptionAttribute("ex");     // Default is "exception"
        //r.setWarnLogCategory("example.MvcLogger");     // No default
        return r;
    }

}