package com.xdkj.admin.system.service;


import com.xdkj.common.model.sysManager.bean.SysManager;

import java.util.List;
import java.util.Map;

public interface SysManagerService {

    void addSysManager(SysManager sysManager);

    void deleteSysManagerById(Integer id);

    int updateSysManager(SysManager sysManager);

    SysManager getSysManagerById(Integer id);

    SysManager getSysManagerByCode(String code);

    List<SysManager> listSysManagersByParams(Map<String, Object> params);

    void loginSuccessInit();
}
