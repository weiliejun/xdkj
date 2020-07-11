package com.xdkj.common.model.sysMessageTmpl.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.sysMessageTmpl.bean.SysMessageTmpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SysMessageTmplDao extends AbstractBaseDao {

    public SysMessageTmpl addSysMessageTmpl(SysMessageTmpl sysMessageTmpl) {
        return (SysMessageTmpl) queryForObject("sysMessageTmpl.addSysMessageTmpl", sysMessageTmpl);
    }

    public void updateSysMessageTmpl(SysMessageTmpl sysMessageTmpl) {
        update("sysMessageTmpl.updateSysMessageTmpl", sysMessageTmpl);
    }

    public SysMessageTmpl getSysMessageTmplById(Integer id) {
        return (SysMessageTmpl) queryForObject("sysMessageTmpl.getSysMessageTmplById", id);
    }

    public List<SysMessageTmpl> listMessageTmplsByParam(Map<String, Object> params) {
        return queryForList("sysMessageTmpl.listMessageTmplsByParam", params);
    }

}
