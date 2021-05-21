package com.example.shirojwttest.config;

import com.example.shirojwttest.filter.JwtFilter;
import com.example.shirojwttest.shiro.UserRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /**
     * 1、自定义身份认证 realm;
     * 必须写这个类，并加上 @Bean 注解，目的是注入 UserRealm，
     * 否则会影响 UserRealm类 中其他类的依赖注入
     */
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

    @Bean
    public JwtFilter jwtFilter(){
        return new JwtFilter();
    }


    //2.DefaultWebSecurityManager
    /**
     * 注入 securityManager
     */
    @Bean("manager")
    public DefaultWebSecurityManager securityManager(
            @Qualifier("userRealm") UserRealm userRealm
    ){

        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm);
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);

        return defaultWebSecurityManager;
    }

    //3.ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            @Qualifier("manager") DefaultWebSecurityManager manager,
            @Qualifier("jwtFilter") JwtFilter jwtFilter
    ){
        ShiroFilterFactoryBean shiroBean = new ShiroFilterFactoryBean();
        shiroBean.setSecurityManager(manager);
        shiroBean.setLoginUrl("/index");
        shiroBean.setUnauthorizedUrl("/noRole");

        Map<String , Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwt", jwtFilter);
        shiroBean.setFilters(filterMap);

        //添加shiro的内置过滤器
        /*
         * anon:无需认证就可以访问
         * authc:必须认证了才能访问
         * user:必须拥有记住我功能才能使用
         * perms:拥有对某个资源的权限才能使用
         * role:拥有某个角色权限才能访问
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/login","anon");
        filterChainDefinitionMap.put("/add","jwt");
        filterChainDefinitionMap.put("/update","jwt");

        shiroBean.setFilterChainDefinitionMap(filterChainDefinitionMap);


        return shiroBean;
    }

}
