package com.xdkj.admin;

import com.github.pagehelper.PageHelper;
import com.xdkj.common.web.filter.CustomCookieFilter;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.Filter;
import java.util.Properties;

//@EnableAutoConfiguration
//@Configuration
// 过滤器和监听器
@ServletComponentScan
// 使@ConfigurationProperties生效
@EnableConfigurationProperties
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("com.xdkj")
@EnableScheduling
@EnableTransactionManagement
@EnableAsync // 启动异步调用
public class Admin extends SpringBootServletInitializer {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Admin.class, args);

        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow", "[]|{}^\"\\<\\>");

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Admin.class);
    }

    @Bean
    PageHelper pageHelper() {
        //分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);

        //添加插件
        new SqlSessionFactoryBean().setPlugins(new Interceptor[]{pageHelper});
        return pageHelper;
    }

    /*@Bean
    public FilterRegistrationBean customCookieFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(customCookieFilter());
        registration.addUrlPatterns("/*");
        registration.setName("customCookieFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }*/

    /**
     * 创建一个bean
     *
     * @return
     */
    /*@Bean(name = "customCookieFilter")
    public Filter customCookieFilter() {
        return new CustomCookieFilter();
    }*/


    /**
     * 文件上传配置
     *
     * @return 与百度编辑器冲突，所以注释了
     */
    /*@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize(DataSize.ofMegabytes(30));
        //该方法已降级
        //factory.setMaxRequestSize("30MB");
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(120));
        return factory.createMultipartConfig();
    }*/
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory>
    containerCustomizer() {
        return new EmbeddedTomcatCustomizer();
    }

    private static class EmbeddedTomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

        @Override
        public void customize(TomcatServletWebServerFactory factory) {
            factory.addConnectorCustomizers(connector -> {
                connector.setAttribute("relaxedPathChars", "<>[\\]^`{|}");
                connector.setAttribute("relaxedQueryChars", "<>[\\]^`{|}");
            });
        }
    }

    /**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是上面的记住我 cookie
     * 记住我需要一个cookie session管理 也需要自己的cookie
     * @return
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("JSESSION_ID");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。
        //它有以下特点：
        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setSecure(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }
}
