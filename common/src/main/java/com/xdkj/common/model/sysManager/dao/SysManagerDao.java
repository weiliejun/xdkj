package com.xdkj.common.model.sysManager.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.sysManager.bean.SysManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SysManagerDao extends AbstractBaseDao {

    public void addSysManager(SysManager sysManager) {
        insert("sysManager.addSysManager", sysManager);
    }

    public void deleteSysManagerById(Integer id) {
        delete("sysManager.deleteSysManagerById", id);
    }

    public int updateSysManager(SysManager sysManager) {
        return update("sysManager.updateSysManager", sysManager);
    }

    public SysManager getSysManagerById(Integer id) {
        return (SysManager) queryForObject("sysManager.getSysManagerById", id);
    }

    public SysManager getSysManagerByCode(String code) {

        return (SysManager) queryForObject("sysManager.getSysManagerByCode", code);
    }

    /**
     * @Description 根据动态参数获取用户
     * @auther: zhangkele
     * @UpadteDate: 2019/2/21 11:30
     */
    public List<SysManager> listSysManagersByParams(Map<String, Object> params) {
        return (List<SysManager>) queryForList("sysManager.listSysManagersByParams", params);
    }


}