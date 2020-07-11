package com.xdkj.pc.service.sysMessageManage;

import com.xdkj.common.model.sysMessage.bean.SysMessage;

import java.util.List;
import java.util.Map;

public interface SysMessageService {

    void addSysMessage(SysMessage record);

    int updateSysMessage(SysMessage record);

    SysMessage getSysMessageById(Integer id);

    SysMessage getSysMessageByParams(Map<String, String> params);

    List<Map<String, Object>> listSysMessagesByParams(Map<String, Object> params);

    int countSysMessagesByParams(Map<String, Object> params);

    Map<String, String> getVerifyCodeByForgetPassword(String mobile, String busiType, String validateCode);

    Map<String, String> sendSmsCodeOnline(String mobile, String busiType, String validateCode, String userId);

    Map<String, Object> validateSmsCodeByParams(String mobile, String busiType, String validateCode, String exitSmsCodeSession);

    int countSysMessageNums(Map<String, Object> params);

    /**
     * @Description 发送站内信
     * @auther: xsp
     * @UpadteDate: 2019/3/12 16:15
     */
    Map<String, Object> sendWebSiteMessage(Integer userId, String busiType, Map<String, String> params);

}