package com.xdkj.pc;

import com.xdkj.common.web.interceptor.BasicInterceptor;
import com.xdkj.common.web.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {
    @Autowired
    BasicInterceptor basicInterceptor;

    @Autowired
    SecurityInterceptor securityInterceptor;

    /**
     * 配置拦截器，登录拦截
     */
    public void addInterceptors(InterceptorRegistry registry) {
        //注册基本拦截器
        registry.addInterceptor(basicInterceptor)
                .addPathPatterns("/**");
        //注册权限拦截器
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/account/**",
                        "/xdkj/service/online/*/"
                ).excludePathPatterns("/xdkj/service/online/weatherQuery/",
                "/xdkj/service/online/mobileNumberPlaceQuery/",
                "/xdkj/service/online/ipPlaceQuery/",
                "/xdkj/service/online/drivingQuestionsQuery/");
    }

    /**
     * 配置静态资源
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置不拦截静态资源
        registry.addResourceHandler("/static/**", "/templates/**").
                addResourceLocations("classpath:/static/", "classpath:/templates/");
    }


}