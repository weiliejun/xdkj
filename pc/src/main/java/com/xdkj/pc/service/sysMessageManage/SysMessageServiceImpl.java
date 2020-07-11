package com.xdkj.pc.service.sysMessageManage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xdkj.common.components.RateLimit;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.constant.ResultCode;
import com.xdkj.common.model.sysMessage.bean.SysMessage;
import com.xdkj.common.model.sysMessage.dao.SysMessageDao;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.user.dao.UserInfoDao;
import com.xdkj.common.model.userWebsiteBulletinRead.dao.UserWebsiteBulletinReadDao;
import com.xdkj.common.model.websiteBulletin.dao.WebsiteBulletinDao;
import com.xdkj.common.thirdparty.service.ThirdPartyCallService;
import com.xdkj.common.util.APIUrlHelper;
import com.xdkj.common.util.DateHelper;
import com.xdkj.common.util.RandomUtil;
import com.xdkj.common.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sysMessageService")
public class SysMessageServiceImpl implements SysMessageService {
    @Autowired
    private SysMessageDao sysMessageDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    ThirdPartyCallService thirdPartyCallService;

    @Autowired
    RateLimit rateLimit;

    @Autowired
    private WebsiteBulletinDao websiteBulletinDao;

    @Autowired
    private UserWebsiteBulletinReadDao userWebsiteBulletinReadDao;

    @Autowired
    private SysMessageTmplService sysMessageTmplService;

    public void addSysMessage(SysMessage record) {
        sysMessageDao.addSysMessage(record);
    }

    public int updateSysMessage(SysMessage record) {
        return sysMessageDao.updateSysMessage(record);
    }

    public SysMessage getSysMessageById(Integer id) {
        return sysMessageDao.getSysMessageById(id);
    }

    public SysMessage getSysMessageByParams(Map<String, String> params) {
        return sysMessageDao.getSysMessageByParams(params);
    }

    public List<Map<String, Object>> listSysMessagesByParams(Map<String, Object> params) {
        return sysMessageDao.listSysMessagesByParams(params);
    }

    public int countSysMessagesByParams(Map<String, Object> params) {
        return sysMessageDao.countSysMessagesByParams(params);
    }

    public Map<String, String> getVerifyCodeByForgetPassword(String mobile, String busiType, String validateCode) {
        Map<String, String> model = new HashMap<String, String>();
        String flag = "false";
        String msg = "";
        UserInfo userInfo = userInfoDao.getUserInfoByMobile(mobile);
        if (userInfo == null) {
            model.put("msg", "您输入的手机号不存在");
            return model;
        }
        String userId = String.valueOf(userInfo.getId());
        //调用发短信接口
        model = sendSmsCodeOnline(mobile, busiType, validateCode, userId);
        return model;
    }

    /**
     * @Description 在线调用接口，发送手机短信验证码(平台使用，不收费)
     * @auther: cyp
     * @UpadteDate: 2019-01-25 11:23
     */
    public Map<String, String> sendSmsCodeOnline(String mobile, String busiType, String validateCode, String userId) {


        Map<String, String> model = new HashMap<String, String>();
        String flag = "false";
        String msg = "";

        //接口流量控制
        String rateLimitKey = mobile;
        if (StringHelper.isNotEmpty(rateLimitKey)) {
            Integer expireTime = (int) DateHelper.getSecondDiffNowToDayEnd();
            boolean allowFlag = rateLimit.allow(busiType, rateLimitKey, expireTime, 6);
            if (!allowFlag) {
                model.put("flag", "false");
                model.put("msg", "获取验证码超过6次，请明天再操作");
                return model;
            }
        }

        //组装参数，调用发送手机验证码短消息接口
        Map<String, String> params = new HashMap<String, String>();
        String ordId = RandomUtil.getSerialNumber();
        params.put("signature", "IT服务网");
        params.put("mobile", mobile);
        params.put("code", validateCode);
        //默认有效时间十分钟
        params.put("validation", "10");
        params.put("ordId", ordId);
        params.put("service", APIUrlHelper.APIName.mbigerSmsCodeSend.getService());

        String respcontent = thirdPartyCallService.callThirdPartyAPI(params);
        JSONObject resultJson = JSON.parseObject(respcontent);
        //返回结果
        String status = resultJson.getString("status");
        //接口调用成功处理
        if ("0".equals(status)) {
            //插入系统消息
            SysMessage sysMessage = new SysMessage();
            if (busiType.equals("forgotPassword")) {  //忘记密码
                sysMessage.setTopic("修改密码");
                sysMessage.setContent("恭喜你修改密码成功");
                sysMessage.setRemark("修改密码");

            } else if ("register".equals(busiType)) {
                sysMessage.setTopic("手机号注册");
                sysMessage.setContent("注册成功");
                sysMessage.setRemark("手机号注册");
            } else if ("sssaApiApply".equals(busiType)) {
                sysMessage.setTopic("saas-API申请");
                sysMessage.setContent("申请成功");
                sysMessage.setRemark("saas服务-API申请");
            } else if ("mobileEdit".equals(busiType)) {
                sysMessage.setTopic("修改手机号");
                sysMessage.setContent("恭喜你修改手机号成功");
            } else if ("mobileBind".equals(busiType)) {
                sysMessage.setTopic("修改手机号");
                sysMessage.setContent("恭喜你修改手机号成功");
            }
            if (!"register".equals(busiType) && userId != null) {
                sysMessage.setUserId(Integer.parseInt(userId));
            }
            sysMessage.setType("sms");
            sysMessage.setBusiType(busiType);
            sysMessage.setStatus("1");
            sysMessage.setCreateTime(new Date());
            sysMessage.setDataStatus("0");
            sysMessage.setMobile(mobile);
            sysMessage.setCode(validateCode);
            sysMessageDao.addSysMessage(sysMessage);

            flag = "true";
            msg = "短信验证码已发送至" + StringUtils.left(mobile, 3) + "****" + StringUtils.right(mobile, 4) + "，10分钟内有效";
        }

        model.put("flag", flag);
        model.put("msg", msg);
        return model;
    }

    /**
     * @Description 校验短信验证码1.是否失效2.是否正确
     * @auther: xsp
     * @UpadteDate: 2019/3/4 17:27
     */
    public Map<String, Object> validateSmsCodeByParams(String mobile, String busiType, String validateCode, String exitSmsCodeSession) {
        String flag = "true";
        String message = "校验通过！";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, String> param = new HashMap<String, String>();
        param.put("mobile", mobile);
        param.put("busiType", busiType);
        param.put("type", "sms");
        SysMessage sysMessage = sysMessageDao.getSysMessageByParams(param);
        // 校验1：短信验证码是否正确
        if (sysMessage == null) {
            resultMap.put("result", false);
            resultMap.put("flag", ResultCode.FORGOT_PASSWORD_SMS_CODE_NOT_EXIST.code());
            resultMap.put("msg", ResultCode.FORGOT_PASSWORD_SMS_CODE_NOT_EXIST.message());
            return resultMap;
        }
        // 校验2：短信验证码是否正确
        if (DateHelper.validTimeDifference(sysMessage.getCreateTime()) == false) {
            resultMap.put("result", false);
            resultMap.put("flag", ResultCode.FORGOT_PASSWORD_SMS_CODE_OUTTIME.code());
            resultMap.put("msg", ResultCode.FORGOT_PASSWORD_SMS_CODE_OUTTIME.message());
            return resultMap;
        }
        // 校验3：短信验证码是否正确
        if (StringHelper.isEmpty(exitSmsCodeSession) || !exitSmsCodeSession.equals(validateCode)) {
            resultMap.put("flag", ResultCode.SMS_CODE_ERROR.code());
            resultMap.put("msg", ResultCode.SMS_CODE_ERROR.message());
            return resultMap;
        }
        resultMap.put("flag", flag);
        resultMap.put("msg", message);
        return resultMap;
    }

    /**
     * session系统消息总数,统计系统消息的总数量
     */
    public int countSysMessageNums(Map<String, Object> params) {

        int countUnreadMessageNum = 0;
        String mobile = params.get("mobile").toString();
        UserInfo userInfo = userInfoDao.getUserInfoByMobile(mobile);
        if (userInfo != null) {
            params.clear();
            params.put("status", "1");//未读
            params.put("userId", userInfo.getId());
            //params.put("mobile",mobile);
            //系统消息的未读总数
            int countSysMessages = sysMessageDao.countSysMessagesByParams(params);
            //网站公告的总数
            params.clear();
            params.put("publishStatus", "0");//发布
            params.put("status", "0");//使用
            int websiteBulletinNum = websiteBulletinDao.countWebsiteBulletinsByParams(params);
            //用户发布状态网站公告已读数
            params.clear();
            params.put("userId", userInfo.getId());
            params.put("publishStatus", "0");
            int websiteBulletinReadNum = userWebsiteBulletinReadDao.countUserWebsiteBulletinReadByParam(params);
            //系统消息总数=(个人系统消息+（网站公告总数-用户网站公告已读数）)，放入session中;
            int websiteBulletinReadABsNum = websiteBulletinNum - websiteBulletinReadNum;
            if (websiteBulletinReadABsNum < 0) {
                websiteBulletinReadABsNum = 0;
            }
            countUnreadMessageNum = websiteBulletinReadABsNum + countSysMessages;
            if (countUnreadMessageNum < 0) {
                countUnreadMessageNum = 0;
            }

        }
        return countUnreadMessageNum;
    }

    /**
     * @Description 发送站内信
     * @auther: xsp
     * @UpadteDate: 2019/3/12 16:43
     */
    public Map<String, Object> sendWebSiteMessage(Integer userId, String busiType, Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String type = GlobalConstant.MessageType.WEBSITE;

        Map<String, String> tmpl = sysMessageTmplService.tmplAssignment(busiType, type, params);

        SysMessage sysMessage = new SysMessage();

        if (userId == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "参数userId不能为空");
            return resultMap;
            //throw new ParameterNullPointerException("userId");
        }
        sysMessage.setUserId(userId);
        sysMessage.setBusiType(busiType);
        sysMessage.setType(type);

        String title = tmpl.get("title");
        if (StringUtils.isBlank(title)) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "未定义title模板内容！");
            return resultMap;
            //throw new TemplateInexistenceException("未定义title模板内容！");
        }
        sysMessage.setTopic(title);

        String content = tmpl.get("content");
        if (StringUtils.isBlank(content)) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "未定义content模板内容！");
            return resultMap;
            //throw new TemplateInexistenceException("未定义content模板内容！");
        }
        sysMessage.setContent(content);
        sysMessage.setDataStatus("0");
        sysMessage.setCreateTime(new Date());
        sysMessage.setStatus("1");

        sysMessageDao.addSysMessage(sysMessage);

        resultMap.put("flag", "true");
        resultMap.put("msg", "发送成功");
        return resultMap;
    }

}