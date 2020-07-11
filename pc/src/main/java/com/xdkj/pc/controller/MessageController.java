package com.xdkj.pc.controller;

import com.xdkj.common.components.verifycode.VerifyCodeImageGenarator;
import com.xdkj.common.constant.ApplicationSessionKeys;
import com.xdkj.common.constant.ResultCode;
import com.xdkj.common.model.sysMessage.bean.SysMessage;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.util.DateHelper;
import com.xdkj.common.util.SmsCodeGenrate;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 发送短信验证码、生成图形验证码
 * @auther: cyp
 * @UpadteDate: 2019-01-21 16:00
 */
@Controller
public class MessageController extends AbstractBaseController {

    private Logger logger = Logger.getLogger(MessageController.class);

    @Autowired
    private SysMessageService sysMessageService;

    /**
     * @Description 生成短信验证码，在线发送，存到session
     * @auther: cyp
     * @UpadteDate: 2019-01-21 16:00
     */
    @RequestMapping(value = {"/user/mobile/SmsCodeSend"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, ?> getMobileVerifyCode(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "mobile", required = true) String mobile, @RequestParam(value = "busiType", required = true) String busiType) {

        //生成手机短信验证码
        String validateCode = String.valueOf(SmsCodeGenrate.getRandomVerifyCode());
        request.getSession().setAttribute(ApplicationSessionKeys.SMS_VERIFY_CODE, validateCode);

        //忘记密码发短信
        Map<String, String> params = sysMessageService.getVerifyCodeByForgetPassword(mobile, busiType, validateCode);

        return params;
    }

    /**
     * @Description 检查用户输入的手机短信验证码，reids中获取
     * @auther: cyp
     * @UpadteDate: 2019-01-21 16:00
     */
    @RequestMapping(value = {"/user/mobile/unlogin/checkSmsCode"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, ?> checkSmsCode(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "mobile") String mobile, @RequestParam(value = "validateCode") String validateCode, @RequestParam(value = "graphicValidateCode") String graphicValidateCode, @RequestParam(value = "busiType") String busiType) {
        Map result = new HashMap();
        Map<String, String> param = new HashMap<String, String>();
        param.put("mobile", mobile);
        param.put("busiType", busiType);
        param.put("type", "sms");
        SysMessage sysMessage = sysMessageService.getSysMessageByParams(param);
        //session中存放短信验证码
        String exitSmsCodeSession = (String) request.getSession().getAttribute(ApplicationSessionKeys.SMS_VERIFY_CODE);
        String exitVerifyCode = (String) request.getSession().getAttribute(ApplicationSessionKeys.LOGIN_VERIFYCODE);
        if (!"".equals(mobile) && !"".equals(mobile) && !"".equals(mobile)) {
            UserInfo userInfo = userInfoService.getUserByMobile(mobile);
            if (userInfo == null) {
                result.put("result", false);
                result.put("flag", ResultCode.FORGOT_PASSWORD_MOBILE_NOT_EXIST.code());
                result.put("message", ResultCode.FORGOT_PASSWORD_MOBILE_NOT_EXIST.message());
                return result;
            }
            if (sysMessage == null) {
                result.put("result", false);
                result.put("flag", ResultCode.FORGOT_PASSWORD_SMS_CODE_NOT_EXIST.code());
                result.put("message", ResultCode.FORGOT_PASSWORD_SMS_CODE_NOT_EXIST.message());
                return result;
            }
            if (exitSmsCodeSession == null || !exitSmsCodeSession.equals(validateCode)) {
                result.put("result", false);
                result.put("flag", ResultCode.FORGOT_PASSWORD_SMS_CODE_ERR.code());
                result.put("message", ResultCode.FORGOT_PASSWORD_SMS_CODE_ERR.message());
                return result;
            }
            if (DateHelper.validTimeDifference(sysMessage.getCreateTime()) == false) {
                result.put("result", false);
                result.put("flag", ResultCode.FORGOT_PASSWORD_SMS_CODE_OUTTIME.code());
                result.put("message", ResultCode.FORGOT_PASSWORD_SMS_CODE_OUTTIME.message());
                return result;
            }
            if (exitVerifyCode == null || !exitVerifyCode.equalsIgnoreCase(graphicValidateCode)) {
                result.put("result", false);
                result.put("flag", ResultCode.FORGOT_PASSWORD_GRAPHICVALIDATE_CODE_ERR.code());
                result.put("message", ResultCode.FORGOT_PASSWORD_GRAPHICVALIDATE_CODE_ERR.message());
                return result;
            }
            result.put("result", true);
            result.put("mobile", mobile);
        }
        return result;
    }


    /**
     * @Description 忘记密码保存方法
     * @auther: cyp
     * @UpadteDate: 2019-01-21 16:00
     */
    @RequestMapping(value = {"/user/forgotPassword/save"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, ?> mobileForgotPasswordSave(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "mobile") String mobile, @RequestParam(value = "password") String password, @RequestParam(value = "passwordConfirm") String passwordConfirm) {
        Map<String, String> result = new HashMap<String, String>();

        result = userInfoService.checkAndSavePassword(mobile, password);
        return result;
    }

    /**
     * @Description 生成图形验证码
     * @auther: cyp
     * @UpadteDate: 2019-01-21 15:59
     */
    @RequestMapping(value = {"/user/get/verifycodeImg"}, method = RequestMethod.GET)
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        VerifyCodeImageGenarator vCode = new VerifyCodeImageGenarator(120, 30, 4, 0);
        //session存放图形验证码
        request.getSession().setAttribute(ApplicationSessionKeys.LOGIN_VERIFYCODE, vCode.getCode());
        vCode.write(response.getOutputStream());
    }


}

