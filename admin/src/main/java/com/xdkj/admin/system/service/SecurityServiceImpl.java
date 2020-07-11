package com.xdkj.admin.system.service;


import com.xdkj.common.model.sysFunction.bean.SysFunction;
import com.xdkj.common.model.sysFunction.dao.SysFunctionDao;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.model.sysManagerRole.bean.SysManagerRole;
import com.xdkj.common.model.sysManagerRole.dao.SysManagerRoleDao;
import com.xdkj.common.model.sysRole.bean.SysRole;
import com.xdkj.common.model.sysRole.dao.SysRoleDao;
import com.xdkj.common.model.sysRoleFunction.bean.SysRoleFunction;
import com.xdkj.common.model.sysRoleFunction.dao.SysRoleFunctionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("securityService")
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private SysFunctionDao sysFunctionDao;

    @Autowired
    private SysRoleFunctionDao sysRoleFunctionDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysManagerRoleDao sysManagerRoleDao;

    public void addSysFunction(SysFunction sysFunction) {
        sysFunctionDao.addSysFunction(sysFunction);
    }

    public void updateSysFunction(SysFunction sysFunction) {
        sysFunctionDao.updateSysFunction(sysFunction);
    }

    @Transactional
    public void deleteSysFunctionByCode(String code) {
        cascadeDeleteSysFunctionByCode(code);
    }

    public SysFunction getSysFunctionById(Integer id) {
        return sysFunctionDao.getSysFunctionById(id);
    }

    public SysFunction getSysFunctionByCode(String code) {
        return sysFunctionDao.getSysFunctionByCode(code);
    }

    public Integer countSysFunctionsByParentCode(String parentCode) {
        return sysFunctionDao.countSysFunctionsByParentCode(parentCode);
    }

    public List<String> listFunctionCodeByRoleId(Integer roleId) {
        return sysRoleFunctionDao.listFunctionCodeByRoleId(roleId);
    }

    public List<SysFunction> listAllSysFunctions() {
        return null;
    }

    public List<SysFunction> listSysFunctionsByParentCode(String parentCode) {
        return sysFunctionDao.listSysFunctionsByParentCode(parentCode);
    }

    public List<SysFunction> listRoleFunctions(SysRole sysRole) {
        return null;
    }

    public List<SysFunction> listSysFunctionsOfSysManager(Integer managerId) {
        return null;
    }

    public void addSysRole(SysRole sysRole) {
        sysRoleDao.addSysRole(sysRole);
    }

    @Transactional
    public void deleteSysRoleById(Integer id) {
        sysRoleFunctionDao.deleteSysRoleFunctionByRoleId(id);
        sysManagerRoleDao.deleteSysManagerRoleByRoleId(id);
        sysRoleDao.deleteSysRoleById(id);
    }

    public void updateSysRole(SysRole sysRole) {
        sysRoleDao.updateSysRole(sysRole);
    }

    /**
     * @Description 设置菜单权限
     * @auther: zhangkele
     * @UpadteDate: 2019/2/27 13:04
     */
    @Transactional
    public void grantRoleRights(Integer roleId, String[] functionCodes, SysManager manager) {
        sysRoleFunctionDao.deleteSysRoleFunctionByRoleId(roleId);
        if (functionCodes != null) {
            SysRoleFunction sysRoleFunction = null;
            for (String functionCode : functionCodes) {
                if (!"".equals(functionCode)) {
                    sysRoleFunction = new SysRoleFunction();
                    sysRoleFunction.setCreatorId(manager.getId());
                    sysRoleFunction.setCreatorName(manager.getName());
                    sysRoleFunction.setCreateTime(new Date());
                    sysRoleFunction.setRoleId(roleId);
                    sysRoleFunction.setFunctionCode(functionCode);
                    sysRoleFunctionDao.addSysRoleFunction(sysRoleFunction);
                }
            }
        }
    }

    public SysRole getSysRoleById(Integer id) {
        return sysRoleDao.getSysRoleById(id);
    }

    public SysRole getSysRoleByName(String roleName) {
        return sysRoleDao.getSysRoleByName(roleName);
    }

    public List<SysRole> listSysRolesByParams(Map<String, Object> params) {
        return sysRoleDao.listSysRolesByParams(params);
    }

    public List<SysRole> listAllSysRoles() {
        return sysRoleDao.listSysRolesByParams(null);
    }

    public List<SysRole> listRoleByManagerId(Integer managerId) {
        return sysRoleDao.listRoleByManagerId(managerId);
    }

    public List<SysFunction> listSysFunctionByManagerId(Integer managerId) {
        return sysFunctionDao.listSysFunctionByManagerId(managerId);
    }


    public List<SysFunction> listSysFunctionByManagerIdAndParentCode(Integer managerId, String parentCode) {
        return sysFunctionDao.listSysFunctionByManagerId(managerId, parentCode);
    }

    public List<SysManagerRole> listSysManagerRoleByManagerId(Integer managerId) {
        return sysManagerRoleDao.listSysManagerRoleByManagerId(managerId);
    }

    @Transactional
    public void grantSysManagerRoles(Integer[] roleIds, Integer managerId, SysManager manager) {
        sysManagerRoleDao.deleteSysManagerRoleByManagerId(managerId);
        if (roleIds != null) {
            SysManagerRole sysManagerRole = null;
            for (Integer roleId : roleIds) {
                sysManagerRole = new SysManagerRole();
                sysManagerRole.setManagerId(managerId);
                sysManagerRole.setRoleId(roleId);
                sysManagerRole.setCreateTime(new Date());
                sysManagerRole.setCreatorId(manager.getId());
                sysManagerRole.setCreatorName(manager.getName());
                sysManagerRoleDao.addSysManagerRole(sysManagerRole);
            }
        }
    }

    /**
     * @Description 递归删除功能节点
     * @auther: zhangkele
     * @UpadteDate: 2019/3/12 10:43
     */
    private void cascadeDeleteSysFunctionByCode(String functionCode) {
        Integer count = sysFunctionDao.countSysFunctionsByParentCode(functionCode);
        if (count > 0) {
            List<SysFunction> list = sysFunctionDao.listSysFunctionsByParentCode(functionCode);
            for (SysFunction sysFunction : list) {
                cascadeDeleteSysFunctionByCode(sysFunction.getCode());
            }
        }
        sysRoleFunctionDao.deleteSysRoleFunctionByCode(functionCode);
        sysFunctionDao.deleteSysFunctionByCode(functionCode);
    }


}
