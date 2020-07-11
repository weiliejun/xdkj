package com.xdkj.admin.system.service;


import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.sysMessage.bean.SysMessage;
import com.xdkj.common.model.sysMessage.dao.SysMessageDao;
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
    private SysMessageTmplService sysMessageTmplService;

    public void addSysMessage(SysMessage sysMessage) {
        sysMessageDao.addSysMessage(sysMessage);
    }

    public int updateSysMessage(SysMessage sysMessage) {
        return sysMessageDao.updateSysMessage(sysMessage);
    }

    public SysMessage getSysMessageById(Integer id) {
        return sysMessageDao.getSysMessageById(id);
    }

    public List<SysMessage> listSysMessagesByParams(Map<String, Object> params) {
        return sysMessageDao.listSysMessageByParams(params);
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
