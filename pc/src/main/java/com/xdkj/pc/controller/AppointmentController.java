package com.xdkj.pc.controller;

import com.xdkj.common.constant.ApplicationSessionKeys;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.constant.ResultCode;
import com.xdkj.common.model.customerAppointment.bean.CustomerAppointment;
import com.xdkj.common.model.serviceinfo.bean.ServiceInfo;
import com.xdkj.common.model.sysMessage.bean.SysMessage;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.util.DateHelper;
import com.xdkj.common.util.StringHelper;
import com.xdkj.common.web.SessionUser;
import com.xdkj.pc.service.mbigerServiceManage.MbigerService;
import com.xdkj.pc.service.sysMessageManage.SysMessageService;
import com.xdkj.pc.service.userManage.CustomerAppointmentService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AppointmentController extends AbstractBaseController {
    private static Logger logger = Logger.getLogger(AppointmentController.class);
    @Value("${resource.pictureServerURL}")
    private String pictureServerAccessURL;
    @Autowired
    private CustomerAppointmentService customerAppointmentService;
    @Autowired
    private MbigerService mbigerService;
    @Autowired
    private SysMessageService sysMessageService;

    /**
     * 申请API提交（saas服务申请，需要发送短信）
     *
     * @param request
     * @param serviceType
     * @return
     */
    @RequestMapping("/appointment/apply/{serviceType}/submit")
    public @ResponseBody
    Map<String, ?> appointmentApplySubmit(HttpServletRequest request,
                                          @PathVariable String serviceType,
                                          CustomerAppointment appointment,
                                          @RequestParam(value = "validateCode") String validateCode,
                                          @RequestParam(value = "remoteValidateCode") String remoteValidateCode) {
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
            // 获取session中存放短信验证码、图形验证码
            String exitSmsCodeSession = (String) request.getSession().getAttribute(ApplicationSessionKeys.SMS_VERIFY_CODE);
            String exitValidateCode = (String) request.getSession().getAttribute(ApplicationSessionKeys.LOGIN_VERIFYCODE);
            // 校验3：图形验证码
            if (StringHelper.isEmpty(exitValidateCode) || !exitValidateCode.equalsIgnoreCase(validateCode)) {
                resultMap.put("flag", ResultCode.VALIDATE_CODE_ERROR.code());
                resultMap.put("msg", ResultCode.VALIDATE_CODE_ERROR.message());
                return resultMap;
            }
            // 校验4：短信验证码是否正确
            if (!userInfo.getMobile().equals(appointment.getCustomerPhone())) {
                if (StringHelper.isEmpty(exitSmsCodeSession) || !exitSmsCodeSession.equals(remoteValidateCode)) {
                    resultMap.put("flag", ResultCode.SMS_CODE_ERROR.code());
                    resultMap.put("msg", ResultCode.SMS_CODE_ERROR.message());
                    return resultMap;
                }
                //校验：短信验证码是否过期
                Map<String, String> param = new HashMap<String, String>();
                param.put("mobile", appointment.getCustomerPhone());
                param.put("busiType", "sssaApiApply");
                param.put("type", "sms");
                SysMessage sysMessage = sysMessageService.getSysMessageByParams(param);
                if (DateHelper.validTimeDifference(sysMessage.getCreateTime()) == false) {
                    resultMap.put("flag", ResultCode.SMS_CODE_TIMEOUT.code());
                    resultMap.put("msg", ResultCode.SMS_CODE_TIMEOUT.message());
                    return resultMap;
                }
            }
            if (StringHelper.isEmpty(serviceType) || "undefined".equals(serviceType)) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "当前服务地址不存在");
                logger.info("===当前请求地址：/appointment/" + serviceType + "/submit");
                return resultMap;
            }
            // 'PAAS','IAAS','CUSTOMIZATION' 三部分预约咨询手机号，姓名为登录用户
            if (StringHelper.isEmpty(appointment.getCustomerName()) || StringHelper.isEmpty(appointment.getCustomerPhone())) {
                appointment.setCustomerName(userInfo.getNickName());
                appointment.setCustomerPhone(userInfo.getMobile());
            }
            appointment.setUserId(userInfo.getId());
            appointment.setServiceCode(serviceType);
            appointment.setStatus("1"); //（联系-0，未联系-1）
            appointment.setCreateTime(new Date());
            appointment.setDataStatus("0");
            customerAppointmentService.addCustomerAppointment(appointment);

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

    /**
     * 服务咨询提交（paas，iaas，定制服务）
     *
     * @param request
     * @param serviceType
     * @return
     */
    @RequestMapping("/appointment/consult/{serviceType}/submit")
    public @ResponseBody
    Map<String, ?> appointmentConsultSubmit(HttpServletRequest request, @PathVariable String serviceType, CustomerAppointment appointment) {
        String flag = "true";
        String msg = "提交成功！";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            if (StringHelper.isEmpty(serviceType) || "undefined".equals(serviceType)) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "当前服务地址不存在");
                logger.info("===当前请求地址：/appointment/" + serviceType + "/submit");
                return resultMap;
            }
            SessionUser sessionUser = getSessionUserBySid(request);
            UserInfo userInfo = sessionUser.getUserInfo();
            appointment.setUserId(userInfo.getId());
            appointment.setServiceCode(serviceType);
            appointment.setStatus("1"); //（联系-0，未联系-1）
            appointment.setCreateTime(new Date());
            appointment.setDataStatus("0");
            appointment.setCustomerName(userInfo.getNickName());
            appointment.setCustomerPhone(userInfo.getMobile());
            customerAppointmentService.addCustomerAppointment(appointment);

        } catch (Exception e) {
            logger.error("msg", e);
            e.printStackTrace();
            resultMap.put("flag", "false");
            resultMap.put("msg", "系统异常，请联系管理员！");
            return resultMap;
        }
        resultMap.put("flag", flag);
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * 文档下载（未使用资源服务器，直接返回文档地址）
     *
     * @param
     * @return
     */
    @RequestMapping("/appointment/{serviceType}/downloadlocal")
    public String docDownloadFromLocal(@PathVariable String serviceType) {
        {
            String url = "true";
            try {
                ServiceInfo serviceInfo = mbigerService.getServiceInfoByCode(serviceType);
                if (serviceInfo == null || StringHelper.isEmpty(serviceInfo.getDocLink())) {
                    logger.info("===当前请求地址：/appointment/" + serviceType + "/submit");
                    return null;
                }
                url = "forward:/static/doc/" + serviceInfo.getDocLink();

                logger.info("文件路径:   " + url + "\n");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return url;

        }
    }

    /**
     * 文档下载(借助浏览器，下载文件)（未使用资源服务器，直接返回文档地址）
     * @param
     * @return
     */
   /* @RequestMapping("/appointment/{serviceType}/download")
    public @ResponseBody ResponseEntity<byte[]> docDownloadFromStream( @PathVariable String serviceType) {
        InputStream fis = null;
        HttpHeaders headers = null;
        byte[] body = null;
        try{
            //  本项目地址下载，借助浏览器，下载文件*//*
            ServiceInfo serviceInfo =  mbigerService.getServiceInfoByCode(serviceType );
            if(serviceInfo ==null || serviceInfo.getDocLink()== null || serviceInfo.getDocLink().length()<=0 ){
                return null;
            }
            logger.info("文件路径:   doc/"+ serviceInfo.getDocLink()+"\n");
            fis = FileUtils.class.getClassLoader().getResourceAsStream("doc/" + serviceInfo.getDocLink());
            body = new byte[fis.available()];
            fis.read(body);
            headers = new HttpHeaders();
            headers.add("Content-Disposition","attachment; filename=" + new String(serviceInfo.getDocLink().getBytes("UTF-8"),"iso-8859-1"));

            *//****** 远程文件地址，借助浏览器，下载文件
     String basePath = pictureServerAccessURL +serviceInfo.getDocLink();
     File file = new File(basePath);
     fis = new FileInputStream(file);
     body = new byte[fis.available()];
     fis.read(body);
     headers = new HttpHeaders();
     headers.add("Content-Disposition","attachment; filename=" + new String(file.getName().getBytes("UTF-8"),"iso-8859-1"));
     logger.info("文件路径:   "+file.getPath()+"\n");
     *******//*
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
              IOUtils.closeQuietly(fis);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);
    }*/

    /**
     * @Description 查询申请次数（未联系）
     * @auther: xsp
     * @UpadteDate: 2019/3/13 15:39
     */
    @RequestMapping("/appointment/getApplyCnt")
    @ResponseBody
    public Map<String, Object> getApplyCnt(HttpServletRequest request, String serviceType) {
        SessionUser sessionUser = getSessionUserBySid(request);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (sessionUser == null) {
            resultMap.put("flag", "nologin");
            resultMap.put("msg", "未登录，请登录后操作！");
            return resultMap;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", sessionUser.getUserInfo().getId());
        params.put("serviceCode", serviceType);
        params.put("status", "1"); // 未联系
        int cnt = customerAppointmentService.countCustomerAppointmentsByParams(params);
        request.setAttribute("applyCnt", cnt);
        if (cnt >= GlobalConstant.APPLY_API_MAX) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "您提交申请次数过多，请等待审核结果");
            return resultMap;
        }
        resultMap.put("flag", "true");
        resultMap.put("applyCnt", cnt);
        return resultMap;
    }
}
