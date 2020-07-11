package com.xdkj.admin.portal.controller;

import com.xdkj.admin.components.shiro.PasswordHelper;
import com.xdkj.admin.system.service.SecurityService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.ApplicationSessionKeys;
import com.xdkj.common.util.StringHelper;
import com.xdkj.admin.system.service.SysManagerService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 后台门户管理
 * @auther: zhangkele
 * @UpadteDate: 2019/2/20 16:33
 */
@Controller
@RequestMapping("/portal")
public class PortalController extends AbstractBaseController {

    @Autowired
    SysManagerService sysManagerService;

    @Autowired
    SecurityService securityService;

    /**
     * @Description 跳转登录页面
     * @auther: zhangkele
     * @UpadteDate: 2019/2/20 16:47
     */
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() {
        return "/login";
    }


    /**
     * @Description 登录认证
     * @auther: zhangkele
     * @UpadteDate: 2019/2/20 16:48
     */
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, ?> onLogin(
            HttpServletRequest request,
            @RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "imageCode", required = true) String imageCode,
            @RequestParam(value = "rememberMe") boolean rememberMe) {
        Map<String, String> resultMap = new HashMap<String, String>();
        String flag = "false";
        String message = "";
        //session中的图形验证码
        String sessionImageCode = request.getSession()
                .getAttribute(ApplicationSessionKeys.LOGIN_VERIFYCODE)
                .toString();

        Subject subject = null;
        if (StringHelper.isEmpty(imageCode)) {
            flag = "false";
            message = "验证码不能为空，请填写！";
        } else if (!imageCode.equalsIgnoreCase(sessionImageCode)) {
            flag = "false";
            message = "您输入的验证码不正确，请核对！";
        } else {//身份认证
            try {
                //对密码进行加密后验证
                UsernamePasswordToken token = new UsernamePasswordToken(userName, PasswordHelper.encryptPassword(userName, password), rememberMe);
                subject = SecurityUtils.getSubject();
                subject.login(token);
                flag = "true";
                message = "登录成功	！";
            } catch (LockedAccountException lae) {
                flag = "false";
                message = "账户被锁定，请联系管理员！";
            } catch (IncorrectCredentialsException ice) {
                // 捕获密码错误异常
                flag = "false";
                message = "您输入的密码不正确，请核对！";
            } catch (UnknownAccountException uae) {
                // 捕获未知用户名异常
                flag = "false";
                message = "用户不存在！";
            } catch (ExcessiveAttemptsException eae) {
                flag = "false";
                message = "密码连续输错3次，账户被锁定！";
            } catch (DisabledAccountException e) {
                flag = "false";
                message = "账户无效，请联系管理员！";
            }
        }
        //登录成功处理
        if (subject != null && subject.isAuthenticated()) {
            sysManagerService.loginSuccessInit();
        }
        resultMap.put("flag", flag);
        resultMap.put("msg", message);
        return resultMap;
    }

    /**
     * @Description 登出
     * @auther: xsp
     * @UpadteDate: 2019/2/22 17:10
     */
    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    public String loginout(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/portal/login";
    }
}