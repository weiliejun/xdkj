package com.xdkj.common.web.interceptor;

import com.xdkj.common.web.SessionUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 基础拦截器
 */
@Component
public class BasicInterceptor extends HandlerInterceptorAdapter {
    private static final Log logger = LogFactory.getLog(BasicInterceptor.class);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    private Date processStartTime = null;


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        processStartTime = new Date();
        String path = request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        logger.info("Execute Time：" + dateFormat.format(new Date()) + " Execute Request：" + path);
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        if (request.getServerPort() == 80) {
            basePath = request.getScheme() + "://" + request.getServerName() + request.getContextPath() + "/";
        }
        logger.info("basePath ：" + basePath);

        //添加登录标识
        /*SessionUser sessionUser = null;
        if (sessionUser != null) {
            request.getSession().setAttribute("isLogin", "true");
        } else {
            request.getSession().setAttribute("isLogin", "false");
        }*/
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Date processEndTime = new Date();
        logger.info("Execute Duration：" + (processEndTime.getTime() - processStartTime.getTime()) + "ms");

        String path = request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String path = request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
    }

}
