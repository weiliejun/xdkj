package com.xdkj.admin;

import com.xdkj.admin.web.interceptor.SessionInterceptor;
import com.xdkj.common.web.interceptor.AvoidDuplicateSubmissionInterceptor;
import com.xdkj.common.web.interceptor.BasicInterceptor;
import com.xdkj.common.web.util.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {
	@Autowired
	BasicInterceptor basicInterceptor;

	@Autowired
	SessionInterceptor sessionInterceptor;

	@Autowired
	AvoidDuplicateSubmissionInterceptor avoidDuplicateSubmissionInterceptor;


	/**
	 * 配置拦截器，登录拦截
	 */
	public void addInterceptors(InterceptorRegistry registry) {
		//注册基本拦截器
		registry.addInterceptor(basicInterceptor)
				.addPathPatterns("/**");
		//注册自动登录拦截器
		registry.addInterceptor(sessionInterceptor)
		               .addPathPatterns("/**")
		                 .excludePathPatterns("/portal/login","/port/logout",
			                        "/error","/get/imgcode");
		//注册自动登录拦截器
		registry.addInterceptor(avoidDuplicateSubmissionInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/portal/login","/port/logout",
						"/error","/get/imgcode");
	}
	/**
	 * 配置静态资源
	 */
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//配置不拦截静态资源
        registry.addResourceHandler("/static/**","/templates/**").
				addResourceLocations("classpath:/static/","classpath:/templates/");
	}

	/**
	 * 配置日期转换器
	 */
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new DateConverter());
	}

	/**
	 * 配置Controller控制类
	 */
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("welcome");
		registry.addViewController("/welcome").setViewName("welcome");
		registry.addViewController("/main").setViewName("main");
	}


}