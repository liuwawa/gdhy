package io.renren.config;

import io.renren.modules.sys.oauth2.OAuth2Filter;
import io.renren.modules.sys.oauth2.OAuth2Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-04-20 18:33
 */
@Configuration
public class ShiroConfig {

    @Bean("sessionManager")
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new OAuth2Filter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/captcha.jpg", "anon");//验证码
        filterMap.put("/favicon.ico", "anon");//网站图标
        filterMap.put("/druid/**", "anon");//连接池监控
        filterMap.put("/api/**", "anon");//移动端接口
        filterMap.put("/sys/login", "anon");//系统登录
        filterMap.put("/css/**", "anon");//css资源文件
        filterMap.put("/fonts/**", "anon");//字体资源文件
        filterMap.put("/img/**", "anon");//静态图片资源文件
        filterMap.put("/js/**", "anon");//js资源文件
        filterMap.put("/libs/**", "anon");//前端依赖资源文件
        filterMap.put("/plugins/**", "anon");//前端插件资源文件

        filterMap.put("/modules/**", "anon");//html资源文件
        filterMap.put("/index.html", "anon");//html资源文件
        filterMap.put("/login.html", "anon");//html资源文件
        filterMap.put("/main.html", "anon");//html资源文件
        filterMap.put("/user.protocol.html", "anon");//用户协议html资源文件
        filterMap.put("/user.permit.html", "anon");//用户发布内容许可资源文件

//        filterMap.put("/webjars/**", "anon");
//        filterMap.put("/**/*.css", "anon");
//        filterMap.put("/**/*.js", "anon");
//        filterMap.put("/**/*.html", "anon");
//        filterMap.put("/**/*.png", "anon");
//        filterMap.put("/fonts/**", "anon");
//        filterMap.put("/plugins/**", "anon");
//        filterMap.put("/swagger/**", "anon");

        filterMap.put("/", "anon");
        filterMap.put("/**", "oauth2");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
