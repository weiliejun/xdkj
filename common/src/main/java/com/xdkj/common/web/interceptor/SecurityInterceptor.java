package com.xdkj.common.web.interceptor;


import com.xdkj.common.redis.service.CacheService;
import com.xdkj.common.redis.util.RedisEnum;
import com.xdkj.common.redis.util.RedisUtil;
import com.xdkj.common.web.SessionUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Enumeration;

/**
 * 权限验证	拦截器
 */
@Component
public class SecurityInterceptor extends HandlerInterceptorAdapter {
    private static final Log logger = LogFactory.getLog(SecurityInterceptor.class);

    @Autowired
    private CacheService cacheService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String key = RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{request.getSession().getId()});
        SessionUser sessionUser = (SessionUser) cacheService.getObject(key);
		if (sessionUser == null) {
			String returnUrl = null;
			if(isAjaxRequest(request)){
				returnUrl = getReferer(request);
			}else{
				returnUrl = request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
			}
			StringBuilder redirectUrl = new StringBuilder();
			redirectUrl.append(request.getContextPath());
			redirectUrl.append("/login");
			if(returnUrl != null){
				redirectUrl.append("?returnUrl=");
				redirectUrl.append(URLEncoder.encode(returnUrl.replaceFirst(request.getContextPath(), "")));
			}
			response.sendRedirect(redirectUrl.toString());
			return false;
		}
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    /**
     * 判断是否是ajax请求
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        boolean isAjax = false;
        String XRequested = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(XRequested)) {
            isAjax = true;
        }
        return isAjax;
    }

    /**
     * 获取referer
     */
    private String getReferer(HttpServletRequest request) {
        Enumeration e = request.getHeaders("Referer");
        String referer = null;
        if (e.hasMoreElements()) {
            referer = (String) e.nextElement();
        }
        return referer;
    }


}
