package com.xdkj.common.model.sysRoleFunction.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.sysRoleFunction.bean.SysRoleFunction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRoleFunctionDao extends AbstractBaseDao {

    public void addSysRoleFunction(SysRoleFunction sysRoleFunction) {
        insert("sysRoleFunction.addSysRoleFunction", sysRoleFunction);
    }

    public void updateSysRoleFunction(SysRoleFunction sysRoleFunction) {
        update("sysRoleFunction.updateSysRoleFunction", sysRoleFunction);
    }

    public void deleteSysRoleFunctionByCode(String functionCode) {
        delete("sysRoleFunction.deleteSysRoleFunctionByCode", functionCode);
    }

    public void deleteSysRoleFunctionByRoleId(Integer roleId) {
        delete("sysRoleFunction.deleteSysRoleFunctionByRoleId", roleId);
    }

    public SysRoleFunction getSysRoleFunctionById(Integer id) {
        return (SysRoleFunction) queryForObject("sysRoleFunction.getSysRoleFunctionById", id);
    }

    public List<String> listFunctionCodeByRoleId(Integer roleId) {
        return (List<String>) queryForList("sysRoleFunction.listFunctionCodeByRoleId", roleId);
    }

}