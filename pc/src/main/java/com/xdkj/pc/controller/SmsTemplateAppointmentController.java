package com.xdkj.pc.controller;

import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.userSmsTemplateApplication.bean.UserSmsTemplateApplication;
import com.xdkj.common.web.SessionUser;
import com.xdkj.pc.service.userManage.UserSmsTemplateApplicationService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SmsTemplateAppointmentController extends AbstractBaseController {
    private static Logger logger = Logger.getLogger(SmsTemplateAppointmentController.class);
    @Autowired
    private UserSmsTemplateApplicationService userSmsTemplateApplicationService;

    /**
     * 短信签名申请API提交
     *
     * @param request
     * @param serviceType
     * @return
     */
    @RequestMapping("/smsTemplateAppointment/apply/{serviceType}/submit")
    public @ResponseBody
    Map<String, ?> appointmentApplySubmit(HttpServletRequest request,
                                          @PathVariable String serviceType,
                                          UserSmsTemplateApplication userSmsTemplateApplication
    ) {
        String flag = "true";
        String message = "提交成功！";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {

            SessionUser sessionUser = getSessionUserBySid(request);
            if (sessionUser == null) {
                resultMap.put("flag", "nologin");
                resultMap.put("msg", "未登录，请登录后操作！");
                return resultMap;
            }

            UserInfo userInfo = sessionUser.getUserInfo();
            userSmsTemplateApplication.setUserId(userInfo.getId());
            userSmsTemplateApplication.setMobile(userInfo.getMobile());
            userSmsTemplateApplication.setServiceProviders("IT服务网");
            userSmsTemplateApplication.setTemplateName(serviceType);
            userSmsTemplateApplication.setReviewStatus("2"); //（0:通过；1：拒绝；2:待审核（审核状态））
            userSmsTemplateApplication.setCreateTime(new Date());
            userSmsTemplateApplication.setDataStatus("0");
            userSmsTemplateApplicationService.addUserSmsTemplateApplication(userSmsTemplateApplication);

        } catch (Exception e) {
            logger.error("msg", e);
            e.printStackTrace();
            flag = "false";
            message = "系统异常！";
        }
        resultMap.put("flag", flag);
        resultMap.put("msg", message);
        return resultMap;
    }
}
