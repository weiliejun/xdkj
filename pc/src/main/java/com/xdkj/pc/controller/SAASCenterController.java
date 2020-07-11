package com.xdkj.pc.controller;

import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.userSmsTemplateApplication.bean.UserSmsTemplateApplication;
import com.xdkj.common.web.SessionUser;
import com.xdkj.pc.service.mbigerServiceManage.MbigerService;
import com.xdkj.pc.service.userManage.CustomerAppointmentService;
import com.xdkj.pc.service.userManage.UserSmsTemplateApplicationService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description SAAS接口调用
 * @Author 徐赛平
 * @UpdateDate 2019/1/18 16:40
 */
@Controller
public class SAASCenterController extends AbstractBaseController {

    @Autowired
    MbigerService mbigerService;

    @Autowired
    private UserSmsTemplateApplicationService userSmsTemplateApplicationService;

    @Autowired
    private CustomerAppointmentService customerAppointmentService;

    /**
     * @Description 导航栏中的---SAAS服务页面
     * @auther: xsp
     * @UpadteDate: 2019/1/21 16:15
     */
    @RequestMapping("/saas/{serviceType}")
    public String saasServiceIndex(HttpServletRequest request, @PathVariable("serviceType") String serviceType, Model model) {
        SessionUser sessionUser = getSessionUserBySid(request);
        if (sessionUser != null) {
            UserInfo userInfo = sessionUser.getUserInfo();
            model.addAttribute("customerName", userInfo.getUserName());
            model.addAttribute("customerPhone", userInfo.getMobile());
        }
        model.addAttribute("serviceType", serviceType);
        return "saas/" + serviceType;
    }

    /**
     * @Description 账户页面---SAAS服务页面
     * @auther: xsp
     * @UpadteDate: 2019/1/21 16:15
     */
    @RequestMapping("/account/saasCenter/{serviceType}")
    public String saasCenterIndex(HttpServletRequest request, @PathVariable("serviceType") String serviceType, Model model) {
        UserInfo userInfo = getUserInfoBySid(request);
        //收费接口
        if (!"weatherQuery".equals(serviceType) &&
                !"mobileNumberPlaceQuery".equals(serviceType) &&
                !"ipPlaceQuery".equals(serviceType) &&
                !"drivingQuestionsQuery".equals(serviceType)) {
            //单次调用费用
            BigDecimal singleCost = mbigerService.getSingleCost(serviceType, GlobalConstant.DEFAULT);
            //剩余免费使用次数
            Integer remainderFreeCount = mbigerService.getRemainderFreeCount(serviceType, GlobalConstant.DEFAULT, String.valueOf(userInfo.getId()));
            //重新查询用户金额
            userInfo = userInfoService.getUserById(userInfo.getId());
            model.addAttribute("accountBalance", userInfo.getUserAccountBalance());
            model.addAttribute("remainderFreeCount", remainderFreeCount);
            model.addAttribute("singleCost", singleCost);

            //云短信申请短信签名统计有效个数
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userInfo.getId());
            params.put("reviewStatusFail", "1");//(未审核，审核已通过)
            int countNum = userSmsTemplateApplicationService.countUserSmsTemplateApplicationsByParams(params);
            params.remove("reviewStatusFail");
            params.put("reviewStatus", "0");//通过审核
            List<UserSmsTemplateApplication> userSmsTemplateApplicationsList = userSmsTemplateApplicationService.listUserSmsTemplateApplicationsByParams(params);

            boolean applyFlag = false;
            if (countNum < 5) {
                applyFlag = true;
            }
            request.setAttribute("applyFlag", applyFlag);
            request.setAttribute("userId", userInfo.getId());
            request.setAttribute("userSmsTemplateApplications", userSmsTemplateApplicationsList);
        } else {
            // 申请API回显手机号
            request.setAttribute("customerName", userInfo.getUserName());
            request.setAttribute("customerPhone", userInfo.getMobile());
            request.setAttribute("serviceType", serviceType);
        }
        return "userAccount/saasCenter/" + serviceType;
    }

    //检查短信签名的个数
    @RequestMapping("/account/userSmsTemplateApply/checkNumber")
    @ResponseBody
    public boolean checkNumber(HttpServletRequest request, String userId) {
        boolean flag = false;

        //云短信申请短信签名统计有效个数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", Integer.parseInt(userId));
        params.put("reviewStatusFail", "1");//(未审核，审核已通过)
        int countNum = userSmsTemplateApplicationService.countUserSmsTemplateApplicationsByParams(params);
        if (countNum < 5) {
            flag = true;
        }
        return flag;
    }


}
