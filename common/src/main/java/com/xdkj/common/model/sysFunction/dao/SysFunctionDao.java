package com.xdkj.common.model.sysFunction.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.sysFunction.bean.SysFunction;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SysFunctionDao extends AbstractBaseDao {
    /**
     * @Description 新增权限
     * @auther: zhangkele
     * @UpadteDate: 2019/2/21 11:30
     */
    public void addSysFunction(SysFunction sysFunction) {
        insert("sysFunction.addSysFunction", sysFunction);
    }

    /**
     * @Description 修改权限
     * @auther: zhangkele
     * @UpadteDate: 2019/2/21 11:30
     */
    public void updateSysFunction(SysFunction sysFunction) {
        update("sysFunction.updateSysFunction", sysFunction);
    }

    /**
     * @Description 根据id删除权限
     * @auther: zhangkele
     * @UpadteDate: 2019/2/27 9:15
     */
    public void deleteSysFunctionById(Integer id) {
        delete("sysFunction.deleteSysFunctionById", id);
    }

    /**
     * @Description 根据code删除权限
     * @auther: zhangkele
     * @UpadteDate: 2019/2/27 9:15
     */
    public void deleteSysFunctionByCode(String code) {
        delete("sysFunction.deleteSysFunctionByCode", code);
    }

    /**
     * @Description 根据parentCode删除权限
     * @auther: zhangkele
     * @UpadteDate: 2019/2/27 9:15
     */
    public void deleteSysFunctionByParentCode(String parentCode) {
        delete("sysFunction.deleteSysFunctionByParentCode", parentCode);
    }

    /**
     * @Description 根据id获取权限
     * @auther: zhangkele
     * @UpadteDate: 2019/2/21 11:30
     */
    public SysFunction getSysFunctionById(Integer id) {
        return (SysFunction) queryForObject("sysFunction.getSysFunctionById", id);
    }

    public SysFunction getSysFunctionByCode(String code) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        List<SysFunction> sysFunctionList = listSysFunctionsByParams(params);
        if (sysFunctionList != null && sysFunctionList.size() > 0) {
            return sysFunctionList.get(0);
        }
        return null;
    }

    /**
     * @Description 根据动态参数获取权限
     * @auther: zhangkele
     * @UpadteDate: 2019/2/21 11:30
     */
    public List<SysFunction> listSysFunctionsByParams(Map<String, Object> params) {
        return (List<SysFunction>) queryForList("sysFunction.listSysFunctionsByParams", params);
    }

    /**
     * @Description 根据parentCode获取权限
     * @auther: zhangkele
     * @UpadteDate: 2019/2/21 11:30
     */
    public List<SysFunction> listSysFunctionsByParentCode(String parentCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentCode", parentCode);
        return listSysFunctionsByParams(params);
    }

    /**
     * @Description 根据parentCode获取权限数量
     * @auther: zhangkele
     * @UpadteDate: 2019/2/27 14:13
     */
    public Integer countSysFunctionsByParentCode(String parentCode) {
        return (Integer) queryForObject("sysFunction.countSysFunctionsByParentCode", parentCode);
    }

    public List<SysFunction> listSysFunctionByManagerId(Integer managerId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("managerId", managerId);
        return (List<SysFunction>) queryForList("sysFunction.listSysFunctionByManagerId", params);
    }

    public List<SysFunction> listSysFunctionByManagerId(Integer managerId, String parentCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("managerId", managerId);
        params.put("parentCode", parentCode);
        return (List<SysFunction>) queryForList("sysFunction.listSysFunctionByManagerId", params);
    }

}