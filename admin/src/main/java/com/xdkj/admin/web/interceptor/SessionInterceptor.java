package com.xdkj.admin.web.interceptor;

import com.xdkj.admin.system.service.SysManagerService;
import com.xdkj.common.model.sysManager.bean.SysManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 解决记住密码后，自动登录sessin丢失问题
 * @auther: zhangkele
 * @UpadteDate: 2019/3/4 18:21
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    @Autowired
    SysManagerService sysManagerService;


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        Subject subject = SecurityUtils.getSubject();
        //判断用户是通过记住我功能自动登录,此时session失效
        if(!subject.isAuthenticated() && subject.isRemembered()){
            try {
                SysManager sysManager  = (SysManager)subject.getPrincipal();
                //密码验证
                UsernamePasswordToken token = new UsernamePasswordToken(sysManager.getCode(), sysManager.getPassword(),true);
                //把当前用户放入session
                subject.login(token);
                //登录成功处理
                if(subject != null && subject.isAuthenticated()){
                    sysManagerService.loginSuccessInit();
                }
            }catch (Exception e){
                //自动登录失败,跳转到登录页面
                response.sendRedirect(request.getContextPath()+"/portal/login");
                return false;
            }
            if(!subject.isAuthenticated()){
                //自动登录失败,跳转到登录页面
                response.sendRedirect(request.getContextPath()+"/portal/login");
                return false;
            }
        }
        return true;
    }
 

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
    }
 

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)  {
    }
}