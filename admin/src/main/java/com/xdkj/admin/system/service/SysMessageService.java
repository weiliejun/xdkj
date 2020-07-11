package com.xdkj.admin.system.service;


import com.xdkj.common.model.sysMessage.bean.SysMessage;

import java.util.List;
import java.util.Map;

public interface SysMessageService {

    void addSysMessage(SysMessage sysMessage);

    int updateSysMessage(SysMessage sysMessage);

    SysMessage getSysMessageById(Integer id);

    List<SysMessage> listSysMessagesByParams(Map<String, Object> params);

    /**
     * @Description 发送站内信
     * @auther: xsp
     * @UpadteDate: 2019/3/12 16:15
     */
    Map<String, Object> sendWebSiteMessage(Integer userId, String busiType, Map<String, String> params);
}
