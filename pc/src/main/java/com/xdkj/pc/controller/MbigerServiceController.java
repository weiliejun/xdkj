package com.xdkj.pc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xdkj.common.constant.ApplicationSessionKeys;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.constant.ResultCode;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.util.APIUrlHelper;
import com.xdkj.common.util.IpHelper;
import com.xdkj.common.util.StringHelper;
import com.xdkj.common.web.SessionUser;
import com.xdkj.pc.service.mbigerServiceManage.MbigerService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 众大服务接口调用
 * @auther: zhangkele
 * @UpadteDate: 2019/1/23 16:27
 */
@RestController
@RequestMapping("/xdkj/service")
public class MbigerServiceController extends AbstractBaseController {

    @Autowired
    MbigerService mbigerService;

    /**
     * @Description 云查询:全国天气预报查询
     * @auther: zhangkele
     * @UpadteDate: 2019/1/23 16:29
     * expenseType:消费类型：api:api调用；online:在线调用
     * service：接口类型：weatherQuery:众大技术服务【全国天气预报查询】
     * mobileNumberPlaceQuery:众大技术服务【手机号码归属地查询】
     * ipPlaceQuery:众大技术服务【全球 IP 地址查询】
     * drivingQuestionsQuery:众大技术服务【机动车驾考公开题库查询】
     * expressDeliveryQuery:众大技术服务【全球快递物流查询】
     * identityCardIdentify:众大技术服务【全国身份证实名认证】
     * bankCardIdentify:众大技术服务【全国银行卡二、三、四要素实名认证】
     * mobileNumberIdentify:众大技术服务【全国手机号（三网）实名认证】
     * smsCodeSend:众大技术服务【云短信-发送手机验证码短消息】
     * intelligentAssistantQA:众大技术服务【人工智能-智能问答助手】
     */
    @RequestMapping("/{expenseType}/{service}")
    public String weatherQuery(HttpServletRequest request,
                               @PathVariable(value = "expenseType", required = true) String expenseType,
                               @PathVariable(value = "service", required = true) String service) {
        try {
            Map<String, String> resultMap = new HashMap<String, String>();
            UserInfo userInfo = null;
            //api调用
            if ("api".equals(expenseType)) {
                String appKey = request.getParameter("appKey");
                userInfo = userInfoService.getUserInfoByAppKey(appKey);
                if (userInfo == null) {
                    resultMap.put("status", "userInfoNotExist");
                    resultMap.put("msg", "用户信息不存在");
                    return JSON.toJSONString(resultMap);
                }
            } else if ("online".equals(expenseType)) {//在线调用
                // 免费服务需要进行验证码校验
                if (APIUrlHelper.APIName.mbigerWeatherQuery.getService().equals(service)
                        || APIUrlHelper.APIName.mbigerMobileNumberPlaceQuery.getService().equals(service)
                        || APIUrlHelper.APIName.mbigerIpPlaceQuery.getService().equals(service)
                        || APIUrlHelper.APIName.mbigerDrivingQuestionsQuery.getService().equals(service)) {
                    //验证码校验
                    String exitVerifyCode = (String) request.getSession().getAttribute(ApplicationSessionKeys.LOGIN_VERIFYCODE);
                    String validateCode = request.getParameter("validateCode");
                    if (StringHelper.isEmpty(validateCode) || !validateCode.equalsIgnoreCase(exitVerifyCode)) {
                        resultMap.put("status", "validateCodeError");
                        resultMap.put("msg", "验证码错误");
                        return JSON.toJSONString(resultMap);
                    }
                }
                SessionUser sessionUser = getSessionUserBySid(request);
                if (sessionUser != null) {
                    userInfo = sessionUser.getUserInfo();
                }
            } else {
                resultMap.put("status", "urlError");
                resultMap.put("msg", "url路径非法");
                return JSON.toJSONString(resultMap);
            }

            Map<String, String> params = new HashMap<String, String>();
            params.put("expenseType", expenseType);
            //全国天气预报查询
            if (APIUrlHelper.APIName.mbigerWeatherQuery.getService().equals(service)) {
                params.put("chinese_city_name", request.getParameter("chineseCityName"));
                params.put("service", APIUrlHelper.APIName.mbigerWeatherQuery.getService());
            } else if (APIUrlHelper.APIName.mbigerMobileNumberPlaceQuery.getService().equals(service)) {//手机号码归属地查询
                params.put("mobile_number", request.getParameter("mobileNumber"));
                params.put("service", APIUrlHelper.APIName.mbigerMobileNumberPlaceQuery.getService());
            } else if (APIUrlHelper.APIName.mbigerIpPlaceQuery.getService().equals(service)) {
                //IP归属地查询
                params.put("ipv4", request.getParameter("ip"));
                params.put("service", APIUrlHelper.APIName.mbigerIpPlaceQuery.getService());
            } else if (APIUrlHelper.APIName.mbigerDrivingQuestionsQuery.getService().equals(service)) {
                //机动车驾考公开题库查询
                String pageNumber = request.getParameter("pageNumber");
                if (StringHelper.isEmpty(pageNumber)) {
                    pageNumber = "1";
                }
                params.put("page_number", pageNumber);
                params.put("question_subject", request.getParameter("questionSubject"));
                params.put("question_type", request.getParameter("questionType"));
                params.put("service", APIUrlHelper.APIName.mbigerDrivingQuestionsQuery.getService());
            } else if (APIUrlHelper.APIName.mbigerExpressDeliveryQuery.getService().equals(service)) {
                //全球快递物流查询
                params.put("tracking_number", request.getParameter("trackingNumber"));
                params.put("service", APIUrlHelper.APIName.mbigerExpressDeliveryQuery.getService());
            } else if (APIUrlHelper.APIName.mbigerIdentityCardIdentify.getService().equals(service)) {
                //全国身份证实名认证
                params.put("name", request.getParameter("name"));
                params.put("number", request.getParameter("number"));
                params.put("service", APIUrlHelper.APIName.mbigerIdentityCardIdentify.getService());
            } else if (APIUrlHelper.APIName.mbigerBankCardIdentify.getService().equals(service)) {
                //全国银行卡二、三、四要素实名认证
                String card = request.getParameter("card");
                /*if(ValidateUtil.checkBankCard(card)){
                    resultMap.put("status","bankCardError");
                    resultMap.put("msg","请输入正确的银行卡卡号");
                    return JSON.toJSONString(resultMap);
                }*/
                params.put("card", card);
                params.put("name", request.getParameter("name"));
                params.put("id", request.getParameter("id"));
                params.put("mobile", request.getParameter("mobile"));
                params.put("service", APIUrlHelper.APIName.mbigerBankCardIdentify.getService());
            } else if (APIUrlHelper.APIName.mbigerMobileNumberIdentify.getService().equals(service)) {
                //全国手机号（三网）实名认证
                params.put("mobile", request.getParameter("mobile"));
                params.put("name", request.getParameter("name"));
                params.put("id", request.getParameter("id"));
                params.put("service", APIUrlHelper.APIName.mbigerMobileNumberIdentify.getService());
            } else if (APIUrlHelper.APIName.mbigerSmsCodeSend.getService().equals(service)) {
                //云短信-发送手机验证码短消息
                params.put("signature", request.getParameter("signature"));
                params.put("mobile", request.getParameter("mobile"));
                params.put("code", request.getParameter("code"));
                params.put("validation", request.getParameter("validation"));
                params.put("service", APIUrlHelper.APIName.mbigerSmsCodeSend.getService());
            } else if (APIUrlHelper.APIName.mbigerIntelligentAssistantQA.getService().equals(service)) {
                //人工智能-智能问答助手
                params.put("question", request.getParameter("question"));
                params.put("service", APIUrlHelper.APIName.mbigerIntelligentAssistantQA.getService());
            } else {
                resultMap.put("status", "serviceError");
                resultMap.put("msg", "服务不存在");
                return JSON.toJSONString(resultMap);
            }
            //接口调用频次key值
            String rateLimitKey = null;
            if (userInfo != null) {
                rateLimitKey = String.valueOf(userInfo.getId());
                params.put("userId", String.valueOf(userInfo.getId()));
            }
            if (StringHelper.isEmpty(rateLimitKey)) {
                String ip = IpHelper.getClientIpAddress(request);
                rateLimitKey = ip;
            }
            params.put("rateLimitKey", rateLimitKey);
            //调用接口
            String respcontent = mbigerService.processBusiness(params);
            //在线调用，收费接口，返回账户余额、免费次数等信息
            if ("online".equals(expenseType)) {
                if (!"weatherQuery".equals(service) &&
                        !"mobileNumberPlaceQuery".equals(service) &&
                        !"ipPlaceQuery".equals(service) &&
                        !"drivingQuestionsQuery".equals(service)) {
                    userInfo = userInfoService.getUserById(userInfo.getId());
                    //单次调用费用
                    BigDecimal singleCost = mbigerService.getSingleCost(service, GlobalConstant.DEFAULT);
                    //剩余免费使用次数
                    Integer remainderFreeCount = mbigerService.getRemainderFreeCount(service, GlobalConstant.DEFAULT, String.valueOf(userInfo.getId()));
                    JSONObject mbigerData = new JSONObject();
                    mbigerData.put("accountBalance", userInfo.getUserAccountBalance());
                    mbigerData.put("remainderFreeCount", remainderFreeCount);
                    mbigerData.put("singleCost", singleCost);
                    JSONObject resultJson = JSON.parseObject(respcontent);

                    //装载页面显示信息（账户余额、免费次数等信息）
                    resultJson.put("mbigerData", mbigerData);
                    respcontent = JSON.toJSONString(resultJson);
                }
            }

            return respcontent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/checkValidateCode")
    @ResponseBody
    public Map<String, ?> checkValidateCode(HttpServletRequest request, @RequestParam(value = "validateCode") String validateCode) {
        String flag = "true";
        String message = "验证通过！";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String exitValidateCode = (String) request.getSession().getAttribute(ApplicationSessionKeys.LOGIN_VERIFYCODE);
        // 校验：图形验证码
        if (StringHelper.isEmpty(exitValidateCode) || !exitValidateCode.equalsIgnoreCase(validateCode)) {
            resultMap.put("flag", ResultCode.VALIDATE_CODE_ERROR.code());
            resultMap.put("msg", ResultCode.VALIDATE_CODE_ERROR.message());
            return resultMap;
        }
        resultMap.put("flag", flag);
        resultMap.put("msg", message);
        return resultMap;
    }

}
