package com.xdkj.common.web.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义会话Cookie属性
 */
@Order(2)
@WebFilter(urlPatterns = {"/*"}, filterName = "customCookieFilter")
public class CustomCookieFilter implements Filter {
    private static final Log log = LogFactory.getLog(CustomCookieFilter.class);
    private static final String CUSTOM_SESSION_ID = "JSESSIONID";
    private static final String HTTP_ONLY = "HttpOnly";
    private static final String SET_COOKIE = "SET-COOKIE";

    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/ueditor/jsp/", "/config", "/", "/welcome", "/main", "/portal/login", "/port/logout",
                    "/error", "/get/imgcode", "/websiteBulletin/addorupdate")));

    //,".js",".css",".jpeg",".png", ".jgp"
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
        System.out.println("执行CustomCookieFilter");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;
        String sessionId = request.getSession().getId();
        String cookieValue = CUSTOM_SESSION_ID + "=" + sessionId + ";Path=/;" + HTTP_ONLY;
        // ajax无法取得数据，被浏览器拦截了
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, If-Modified-Since");
        response.addHeader("Access-Control-Allow-Credentials", "true");// 允许跨域设置cookie
        response.setHeader(SET_COOKIE, cookieValue);

        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        boolean allowedPath = ALLOWED_PATHS.contains(path);

        if (allowedPath) {
            System.out.println("这里是不需要处理的url进入的方法");
            chain.doFilter(request, response);
        } else {
            System.out.println("这里是需要处理的url进入的方法");
            chain.doFilter(new XssHttpServletRequestWrapper(request), response);
        }

    }

    public void init(FilterConfig fConfig) throws ServletException {
    }


}
