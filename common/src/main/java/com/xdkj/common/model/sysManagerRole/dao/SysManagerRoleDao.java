package com.xdkj.common.model.sysManagerRole.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.sysManagerRole.bean.SysManagerRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysManagerRoleDao extends AbstractBaseDao {

    public void addSysManagerRole(SysManagerRole sysManagerRole) {
        insert("sysManagerRole.addSysManagerRole", sysManagerRole);
    }

    public void updateSysManagerRole(SysManagerRole sysManagerRole) {
        update("sysManagerRole.updateSysManagerRole", sysManagerRole);
    }

    public void deleteSysManagerRoleByManagerId(Integer managerId) {
        delete("sysManagerRole.deleteSysManagerRoleByManagerId", managerId);
    }

    public void deleteSysManagerRoleByRoleId(Integer roleId) {
        delete("sysManagerRole.deleteSysManagerRoleByRoleId", roleId);
    }

    public SysManagerRole getaysManagerRoleById(Integer id) {
        return (SysManagerRole) queryForObject("sysManagerRole.getSysManagerRoleById", id);
    }

    public List<SysManagerRole> listSysManagerRoleByManagerId(Integer managerId) {
        return (List<SysManagerRole>) queryForList("sysManagerRole.listSysManagerRoleByManagerId", managerId);
    }

}