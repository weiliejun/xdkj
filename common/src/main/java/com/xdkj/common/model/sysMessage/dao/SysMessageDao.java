package com.xdkj.common.model.sysMessage.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.sysMessage.bean.SysMessage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SysMessageDao extends AbstractBaseDao {

    public void addSysMessage(SysMessage sysMessage) {
        insert("sysMessage.addSysMessage", sysMessage);
    }

    public int updateSysMessage(SysMessage sysMessage) {
        return update("sysMessage.updateSysMessage", sysMessage);
    }

    public SysMessage getSysMessageById(Integer id) {
        return (SysMessage) queryForObject("sysMessage.getSysMessageById", id);
    }

    public SysMessage getSysMessageByParams(Map<String, String> params) {
        return (SysMessage) queryForObject("sysMessage.getSysMessageByParams", params);
    }

    public List<Map<String, Object>> listSysMessagesByParams(Map<String, Object> params) {
        return queryForList("sysMessage.listSysMessagesByParams", params);
    }

    public int countSysMessagesByParams(Map<String, Object> params) {
        return (int) queryForObject("sysMessage.countSysMessagesByParams", params);
    }

    public List<SysMessage> listSysMessageByParams(Map<String, Object> params) {
        return queryForList("sysMessage.listSysMessageByParams", params);
    }
}