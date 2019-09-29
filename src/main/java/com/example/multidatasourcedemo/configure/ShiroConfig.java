package com.example.multidatasourcedemo.configure;

import com.example.multidatasourcedemo.component.MySessionListener1;
import com.example.multidatasourcedemo.component.MySessionListener2;
import com.example.multidatasourcedemo.pojo.TestRealm;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
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


    @Value("${spring.redis.host}")
    private String host;

    @Value("${shiro.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    /**
     * 创建Realm——自定义Realm类
     */
    @Bean(name = "testRealm")
    public TestRealm getRealm() {
        return new TestRealm();
    }

    /**
     * 创建DefaultWebSecurityManager——关联realm（连接数据库）
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("testRealm") TestRealm testRealm, @Qualifier("sessionManager") DefaultWebSessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setSessionManager(sessionManager);
        //关联realm
        securityManager.setRealm(testRealm);
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * 创建ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
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
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        /*
         * 单个url拦截，
         */
/*		filterChainDefinitionMap.put("/add", "authc");
		filterChainDefinitionMap.put("/update", "authc");*/
        //批量url拦截
        filterChainDefinitionMap.put("/actuator/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        //url放行
        filterChainDefinitionMap.put("/testThymeleaf", "anon");
        filterChainDefinitionMap.put("/add", "user");
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

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor
    authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new
                AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public SimpleCookie getSimpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("SHRIOSESSIONID");
        simpleCookie.setHttpOnly(true);
        //设置cookie过期时间，秒为单位，-1表示浏览器关闭时过期cookie
//        simpleCookie.setMaxAge(1 * 60 * 60);
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    //配置shiro session 的一个管理器
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager getDefaultWebSessionManager(MySessionListener1 mySessionListener1, MySessionListener2 mySessionListener2) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        listeners.add(mySessionListener1);
        listeners.add(mySessionListener2);
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionDAO(sessionDAO());
        //session有效期 默认值1800000 30分钟 1800000毫秒  -1000表示永久
        sessionManager.setGlobalSessionTimeout(600000);
        SimpleCookie simpleCookie = getSimpleCookie();
        sessionManager.setSessionIdCookie(simpleCookie);
        return sessionManager;
    }


    @Bean
    public RedisSessionDAO sessionDAO(){
        // crazycake 实现
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        //  Session ID 生成器
        sessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return sessionDAO;
    }


    @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setTimeout(timeout);
//        redisManager.setPassword(password);
        return redisManager;
    }


    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator(){
        return new JavaUuidSessionIdGenerator();
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
        simpleCookie.setHttpOnly(true);
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /**
     * cookie管理器;
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        //rememberme cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度（128 256 512 位），通过以下代码可以获取
        //KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //SecretKey deskey = keygen.generateKey();
        //System.out.println(Base64.encodeToString(deskey.getEncoded()));
        byte[] cipherKey = Base64.decode("wGiHplamyXlVB11UXWol8g==");
        cookieRememberMeManager.setCipherKey(cipherKey);
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }
}
