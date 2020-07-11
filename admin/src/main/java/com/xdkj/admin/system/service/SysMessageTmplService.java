package com.xdkj.admin.system.service;

import com.xdkj.common.model.sysMessageTmpl.bean.SysMessageTmpl;

import java.util.List;
import java.util.Map;

public interface SysMessageTmplService {

    SysMessageTmpl addSysMessageTmpl(SysMessageTmpl record);


    SysMessageTmpl getSysMessageTmplById(Integer id);


    void updateSysMessageTmpl(SysMessageTmpl record);

    List<SysMessageTmpl> listSysMessageTmplsByParam(Map<String, Object> params);

    /**
     * cyp
     *
     * @param busiType
     * @param type
     * @param values
     * @return 数据库获取模板
     */
    Map<String, String> tmplAssignment(String busiType, String type, Map<String, String> values);
}