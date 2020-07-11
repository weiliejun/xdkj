package com.xdkj.common.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.web.annotations.AvoidDuplicateSubmission;
import com.xdkj.common.web.util.WebUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * 防止重复提交过滤器
 */
@Component
public class AvoidDuplicateSubmissionInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(AvoidDuplicateSubmissionInterceptor.class);

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AvoidDuplicateSubmission annotation = method.getAnnotation(AvoidDuplicateSubmission.class);
            if (annotation != null) {
                boolean needSaveSession = annotation.needSaveToken();
                if (needSaveSession) {
                    session.setAttribute("token", WebUtil.generateToken(session.getId().toString()));
                }
                boolean needRemoveSession = annotation.needRemoveToken();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request)) {
                        SysManager sysManager = (SysManager) subject.getPrincipal();
                        String userName = sysManager.getName();
                        LOG.warn("请不要重复提交,[user:" + userName + ",url:"
                                + request.getServletPath() + "]");
                        //response.sendRedirect(request.getContextPath() + "");
                        if (WebUtil.isAjax(request)) {
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json");
                            JSONObject resultJson = new JSONObject();
                            resultJson.put("flag", "false");
                            resultJson.put("msg", "请不要重复提交");
                            response.getWriter().write(JSON.toJSONString(resultJson));
                        }
                        return false;
                    }
                    session.removeAttribute("token");
                }
            }
        }
        return true;
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession(false).getAttribute("token");
        if (serverToken == null) {
            return true;
        }
        String clinetToken = request.getParameter("token");
        if (clinetToken == null) {
            return true;
        }
        if (!serverToken.equals(clinetToken)) {
            return true;
        }
        return false;
    }


}