package com.xdkj.pc.controller;

import com.xdkj.common.constant.ApplicationSessionKeys;
import com.xdkj.common.constant.ResultCode;
import com.xdkj.common.model.sysMessage.bean.SysMessage;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.util.DateHelper;
import com.xdkj.common.util.SmsCodeGenrate;
import com.xdkj.common.util.StringHelper;
import com.xdkj.common.web.SessionUser;
import com.xdkj.pc.service.sysMessageManage.SysMessageService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RegisterController extends AbstractBaseController {
    private Logger logger = Logger.getLogger(RegisterController.class);
    @Autowired
    private SysMessageService sysMessageService;

    @RequestMapping("/register/index")
    public String registerIndex(HttpServletRequest request) {
        return "/register";
    }

    /**
     * 注册信息提交
     *
     * @param request
     * @param mobile
     * @param password
     * @param nickName
     * @param validateCode       图形码
     * @param remoteValidateCode 短信验证码
     * @return
     */
    @RequestMapping("/register/submit")
    @ResponseBody
    public Map<String, ?> register(HttpServletRequest request, @RequestParam(value = "mobile", required = true) String mobile,
                                   @RequestParam(value = "password", required = true) String password,
                                   @RequestParam(value = "nickName", required = true) String nickName,
                                   @RequestParam(value = "validateCode", required = true) String validateCode/*,
                                   @RequestParam(value = "remoteValidateCode", required = true) String remoteValidateCode*/) {
        // 获取session中存放短信验证码、图形验证码
        String exitSmsCodeSession = (String) request.getSession().getAttribute(ApplicationSessionKeys.SMS_VERIFY_CODE);
        String exitValidateCode = (String) request.getSession().getAttribute(ApplicationSessionKeys.LOGIN_VERIFYCODE);
        // 处理登录逻辑，登录时重新生成session
        /*String sid = request.getSession().getId();
        String ip = IpHelper.getClientIpAddress(request);*/

        //=============业务区1：信息校验 ==================
        // 校验：用户名、手机号、图形验证码
        Map<String, String> resultMap = userInfoService.userRegisterVerify(mobile, nickName, validateCode, exitValidateCode);
        String flag = (String) resultMap.get("flag");
        if (!ResultCode.SUCCESS.code().equals((flag))) {
            return resultMap;
        }
        // 校验4：短信验证码是否正确
        /*if (StringHelper.isEmpty(exitSmsCodeSession) || !exitSmsCodeSession.equals(remoteValidateCode)) {
            resultMap.put("flag", ResultCode.SMS_CODE_ERROR.code());
            resultMap.put("msg", ResultCode.SMS_CODE_ERROR.message());
            return resultMap;
        }*/
        //校验：短信验证码是否过期
        /*Map<String, String> param = new HashMap<String, String>();
        param.put("mobile", mobile);
        param.put("busiType", "register");
        param.put("type", "sms");
        SysMessage sysMessage = sysMessageService.getSysMessageByParams(param);
        if (DateHelper.validTimeDifference(sysMessage.getCreateTime()) == false) {
            resultMap.put("flag", ResultCode.SMS_CODE_TIMEOUT.code());
            resultMap.put("msg", ResultCode.SMS_CODE_TIMEOUT.message());
            return resultMap;
        }*/
        // ============ 业务区2：提交注册信息 ===============
        resultMap = userInfoService.userRegisterSubmit(mobile, password, nickName);

        return resultMap;
    }

    /**
     * @param mobile
     * @param nickName
     * @param validateCode 图形码
     * @Description 生成短信验证码，在线发送，存到session
     * 1. 注册：userId 为空
     * 2. SAAS服务申请API
     * @auther: FENG.yanmin
     * @UpadteDate: 2019-01-25 18:00
     */
    @RequestMapping(value = {"/mobile/smscodeSend"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, ?> getMobileVerifyCodeByRegister(HttpServletRequest request, HttpServletResponse response,
                                                        @RequestParam(value = "nickName", required = true) String nickName,
                                                        @RequestParam(value = "validateCode", required = true) String validateCode,
                                                        @RequestParam(value = "mobile", required = true) String mobile,
                                                        @RequestParam(value = "busiType", required = true) String busiType) {
        //生成[手机短信]验证码
        String smsVerifyCode = String.valueOf(SmsCodeGenrate.getRandomVerifyCode());
        logger.info("缓存session中smscode值为======" + "[" + smsVerifyCode + "]");
        request.getSession().setAttribute(ApplicationSessionKeys.SMS_VERIFY_CODE, smsVerifyCode);
        Map<String, String> resultMap = new HashMap<String, String>();
        if (StringHelper.isEmpty(busiType)) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "短信业务类型异常");
            return resultMap;
        }
        String userId = null;
        //注册发送短信
        if ("register".equals(busiType)) { //发送短信，注册userId传递null
            // 校验：用户名、手机号、图形验证码
            String exitValidateCode = (String) request.getSession().getAttribute(ApplicationSessionKeys.LOGIN_VERIFYCODE);
            resultMap = userInfoService.userRegisterVerify(mobile, nickName, validateCode, exitValidateCode);
            String flag = resultMap.get("flag");
            if (!ResultCode.SUCCESS.code().equals((flag))) {
                return resultMap;
            }
        } else if ("sssaApiApply".equals(busiType)) {  // saas-api申请发送短信
            SessionUser sessionUser = getSessionUserBySid(request);
            if (sessionUser == null) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "未登录，请登录后操作！");
                return resultMap;
            }
            UserInfo checkMoblie = userInfoService.getUserByMobile(mobile);
            if (checkMoblie != null) {
                /*resultMap.put("flag", "false");
                resultMap.put("msg", "您输入的手机号不存在");
                return resultMap;*/
                userId = checkMoblie.getId().toString();
            }
        } else if ("mobileEdit".equals(busiType)) {  // 修改手机号第一步发送短信
            SessionUser sessionUser = getSessionUserBySid(request);
            if (sessionUser == null) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "未登录，请登录后操作！");
                return resultMap;
            }
            UserInfo checkMoblie = userInfoService.getUserByMobile(mobile);
            if (checkMoblie == null) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "您输入的手机号不存在");
                return resultMap;
            }
            userId = checkMoblie.getId().toString();
        } else if ("mobileBind".equals(busiType)) {  // 修改手机号第二步发送短信
            SessionUser sessionUser = getSessionUserBySid(request);
            if (sessionUser == null) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "未登录，请登录后操作！");
                return resultMap;
            }
            UserInfo checkMoblie = userInfoService.getUserByMobile(mobile);
            if (checkMoblie != null) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "你输入的手机号码已被注册");
                return resultMap;
            }
            userId = sessionUser.getUserInfo().getId().toString();
        }
        //发短信
        resultMap = sysMessageService.sendSmsCodeOnline(mobile, busiType, smsVerifyCode, userId);

        return resultMap;
    }


}
