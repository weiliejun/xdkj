package com.xdkj.pc.controller;

import com.xdkj.common.constant.ApplicationSessionKeys;
import com.xdkj.common.util.IpHelper;
import com.xdkj.common.web.SessionUser;
import com.xdkj.pc.service.sysMessageManage.SysMessageService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
public class LoginController extends AbstractBaseController {

    @Autowired
    private SysMessageService sysMessageService;

    /**
     * @author cyp
     * @time : 2019-01-14 16:11
     * @description 跳转到登录页面
     */
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String loginIndex() {
        return "login";
    }

    /**
     * @author cyp
     * @time : 2019-01-14 16:14
     * @description 登录的方法
     */
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, ?> onLogin(HttpServletRequest request,
                                  @RequestParam(value = "userName", required = true) String userName,
                                  @RequestParam(value = "password", required = true) String password,
                                  @RequestParam(value = "validateCode", required = true) String validateCode) {

        String exitVerifyCode = (String) request.getSession().getAttribute(ApplicationSessionKeys.LOGIN_VERIFYCODE);
        // 处理登录逻辑，登录时重新生成session
        String sid = request.getSession().getId();
        String ip = IpHelper.getClientIpAddress(request);
        // 登录
        Map<String, String> result = userInfoService.login(userName, password, ip, sid, exitVerifyCode, validateCode);
        if (result.get("flag") != null && result.get("flag").equalsIgnoreCase("true")) {
            request.getSession().setAttribute(ApplicationSessionKeys.IS_LOGIN, "true");
        }else{
            request.getSession().setAttribute(ApplicationSessionKeys.IS_LOGIN, "false");
        }
        /**
         * session系统消息总数
         */
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", userName);
        int countUnreadMessageNum = sysMessageService.countSysMessageNums(params);
        request.getSession().setAttribute(ApplicationSessionKeys.SYS_MESSAGE_NUMS_COUNT, countUnreadMessageNum);

        return result;
    }

    /**
     * @author cyp
     * @time : 2019-01-16 13:29
     * @description 忘记密码
     */
    @RequestMapping("/user/forgotPassword")
    public String forgotPassword() {
        return "forgotPassword";
    }

    @RequestMapping("/islogin")
    @ResponseBody
    public boolean islogin(HttpServletRequest request) {
        boolean flag = false;
        SessionUser sessionUser = getSessionUserBySid(request);
        if (sessionUser != null) {
            flag = true;
        }
        return flag;
    }
}
