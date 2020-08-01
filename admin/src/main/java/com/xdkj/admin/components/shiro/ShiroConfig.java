package com.xdkj.admin.components.shiro;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ShiroRedisCacheManager shiroRedisCacheManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 没有登陆的用户只能访问登陆页面
        shiroFilterFactoryBean.setLoginUrl("/portal/login");
        //自定义拦截器
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        //限制同一帐号同时在线的个数。
        filtersMap.put("kickout", kickoutSessionControlFilter(shiroRedisCacheManager));
        shiroFilterFactoryBean.setFilters(filtersMap);
        // 权限控制p.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/get/imgcode", "anon");
        filterChainDefinitionMap.put("/portal/login", "kickout,anon");
        filterChainDefinitionMap.put("/portal/logout", "logout");

        filterChainDefinitionMap.put("/upload/**", "anon");
        //配置记住我或认证通过可以访问的地址
        filterChainDefinitionMap.put("/**", "kickout,user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(ShiroRedisCacheManager shiroRedisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(myShiroRealm(shiroRedisCacheManager));
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(shiroRedisCacheManager);
        // 自定义session管理
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * cookie对象;
     * rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

    /**
     * 配置密码比较器
     */
    @Bean
    public RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher(ShiroRedisCacheManager shiroRedisCacheManager) {
        RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(shiroRedisCacheManager);
        return retryLimitHashedCredentialsMatcher;
    }


    /**
     * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
     */
    @Bean
    public MyShiroRealm myShiroRealm(ShiroRedisCacheManager shiroRedisCacheManager) {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher(shiroRedisCacheManager));
        return myShiroRealm;
    }


    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    /**
     * shiro session的管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {  //配置默认的sesssion管理器
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setGlobalSessionTimeout(30 * 60 * 1000);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionDAO(sessionDAO());
        return sessionManager;
    }


    @Bean
    public SessionDAO sessionDAO() {
        return new MemorySessionDAO();//使用默认的MemorySessionDAO
    }

    /**
     * @return
     * @描述：自定义cookie中session名称等配置 发布以后运行以后发现老是会出现：org.apache.shiro.session.UnknownSessionException: There is no session with id [xxxx]的问题，
     * 只所以出现这个问题是因为在shiro的DefaultWebSessionManager类中，默认Cookie名称是JSESSIONID，这样的话与servlet容器名冲突, 如jetty, tomcat等默认JSESSIONID, 当跳出shiro servlet时如error-page容器会为JSESSIONID重新分配值导致登录会话丢失!
     * 　　明白了出现这个问题的原因，就好办了，我们只需要自己指定一个与项目运行容器不冲突的sessionID就好了
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        //如果在Cookie中设置了"HttpOnly"属性，那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息，这样能有效的防止XSS攻击。
        simpleCookie.setHttpOnly(true);
        simpleCookie.setName("shiro.sesssion");
        //单位秒
        simpleCookie.setMaxAge(86400);
        return simpleCookie;
    }

    /**
     * 限制同一账号同时登录人数控制
     */
    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter(ShiroRedisCacheManager shiroRedisCacheManager) {
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        kickoutSessionControlFilter.setCacheManager(shiroRedisCacheManager);
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        kickoutSessionControlFilter.setKickoutAfter(false);
        kickoutSessionControlFilter.setMaxSession(1);
        kickoutSessionControlFilter.setKickoutUrl("/portal/logout");
        return kickoutSessionControlFilter;
    }
}