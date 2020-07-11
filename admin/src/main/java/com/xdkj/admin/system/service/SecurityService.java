package com.xdkj.admin.system.service;


import com.xdkj.common.model.sysFunction.bean.SysFunction;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.model.sysManagerRole.bean.SysManagerRole;
import com.xdkj.common.model.sysRole.bean.SysRole;

import java.util.List;
import java.util.Map;

public interface SecurityService {

    void addSysFunction(SysFunction sysFunction);

    void updateSysFunction(SysFunction sysFunction);

    void deleteSysFunctionByCode(String code);

    SysFunction getSysFunctionById(Integer id);

    SysFunction getSysFunctionByCode(String code);

    Integer countSysFunctionsByParentCode(String parentCode);

    List<String> listFunctionCodeByRoleId(Integer roleId);

    List<SysFunction> listAllSysFunctions();

    List<SysFunction> listSysFunctionsByParentCode(String parentCode);

    List<SysFunction> listRoleFunctions(SysRole sysRole);

    List<SysFunction> listSysFunctionsOfSysManager(Integer managerId);


    void addSysRole(SysRole sysRole);

    void deleteSysRoleById(Integer id);

    void updateSysRole(SysRole sysRole);

    void grantRoleRights(Integer roleId, String[] functionCodes, SysManager manager);

    SysRole getSysRoleById(Integer id);

    SysRole getSysRoleByName(String roleName);

    /**
     * @Description 根据动态条件查询角色列表
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 10:04
     */
    List<SysRole> listSysRolesByParams(Map<String, Object> params);

    List<SysRole> listAllSysRoles();

    List<SysRole> listRoleByManagerId(Integer managerId);

    List<SysFunction> listSysFunctionByManagerId(Integer managerId);

    List<SysFunction> listSysFunctionByManagerIdAndParentCode(Integer managerId, String parentCode);

    List<SysManagerRole> listSysManagerRoleByManagerId(Integer managerId);

    void grantSysManagerRoles(Integer[] roleIds, Integer managerId, SysManager manager);

}
