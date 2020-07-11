package com.xdkj.common.model.sysRole.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.sysRole.bean.SysRole;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SysRoleDao extends AbstractBaseDao {

    public void addSysRole(SysRole sysRole) {
        insert("sysRole.addSysRole", sysRole);
    }

    public void deleteSysRoleById(Integer id) {
        delete("sysRole.deleteSysRoleById", id);
    }

    public void updateSysRole(SysRole sysRole) {
        update("sysRole.updateSysRole", sysRole);
    }

    public SysRole getSysRoleById(Integer id) {
        return (SysRole) queryForObject("sysRole.getSysRoleById", id);
    }

    public SysRole getSysRoleByName(String roleName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", roleName);
        List<SysRole> SysRoleList = listSysRolesByParams(params);
        if (SysRoleList != null && SysRoleList.size() > 0) {
            return SysRoleList.get(0);
        }
        return null;
    }

    /**
     * @Description 根据动态参数获取角色列表
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 10:17
     */
    public List<SysRole> listSysRolesByParams(Map<String, Object> params) {
        return (List<SysRole>) queryForList("sysRole.listSysRolesByParams", params);
    }

    public List<SysRole> listRoleByManagerId(Integer managerId) {
        return (List<SysRole>) queryForList("sysRole.listRoleByManagerId", managerId);
    }

}