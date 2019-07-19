package com.example.multidatasourcedemo.configure;

import com.example.multidatasourcedemo.pojo.TestRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: ShiroConfig
 * @Auther: zhoucc
 * @Date: 2019/7/16 16:07
 * @Description: Shiro的配置类
 */

@Configuration
public class ShiroConfig {

    /**
     * 创建Realm——自定义Realm类
     */
    @Bean(name="testRealm")
    public TestRealm getRealm() {
        return new TestRealm();
    }

    /**
     * 创建DefaultWebSecurityManager——关联realm（连接数据库）
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("testRealm")TestRealm testRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(testRealm);
        return securityManager;
    }

    /**
     * 创建ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        /*
         * 使用Shiro内置过滤器实现页面拦截：拦截url链接请求
         *
         * shiro内置过滤器，可以实现权限相关的拦截器
         * 		常用的过滤器：
         * 			anon:无需认证（登录）可以访问
         * 			authc:必须认证才可以访问
         * 			user:如果使用rememberMe的功能可以直接访问  （记住用户和密码）
         * 			perms:该资源必须得到资源权限才可以访问		（密码验证）
         * 			role:该资源必须得到角色权限才可以访问		（VIP会员）
         *
         */
        //创建集合——充作拦截器集合
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        /*
         * 单个url拦截，
         */
/*		filterChainDefinitionMap.put("/add", "authc");
		filterChainDefinitionMap.put("/update", "authc");*/
        //批量url拦截
        filterChainDefinitionMap.put("/login", "anon");
        //url放行
        filterChainDefinitionMap.put("/testThymeleaf", "anon");
        filterChainDefinitionMap.put("/*", "authc");
        /*
         * shiro拦截器拦截成功后，会返回一个默认的地址login.jsp
         * 		可以自定义修改调整的登录页面
         */
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/testThymeleaf");

        //设置拦截器map集合
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }
}
