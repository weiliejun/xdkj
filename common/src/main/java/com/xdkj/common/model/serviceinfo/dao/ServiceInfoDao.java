package com.xdkj.common.model.serviceinfo.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.serviceinfo.bean.ServiceInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ServiceInfoDao extends AbstractBaseDao {

    public ServiceInfo addServiceInfo(ServiceInfo serviceInfo) {
        insert("serviceinfo.addServiceInfo", serviceInfo);
        return serviceInfo;
    }

    public void updateServiceInfo(ServiceInfo serviceInfo) {
        update("serviceinfo.updateServiceInfo", serviceInfo);
    }

    public ServiceInfo getServiceInfoById(Integer id) {
        return (ServiceInfo) queryForObject("serviceinfo.getServiceInfoById", id);
    }

    /**
     * @Description 根据动态参数查询ServiceInfo列表
     * @auther: zhangkele
     * @UpadteDate: 2019/1/23 16:59
     */
    public List<ServiceInfo> listServiceInfosByParams(Map<String, Object> params) {
        return (List<ServiceInfo>) queryForList("serviceinfo.listServiceInfosByParams", params);
    }

    /**
     * @Description 根据serviceCode查询ServiceInfo
     * @auther: FENG.yanmin
     * @UpadteDate: 2019/1/23 17:01
     */
    public ServiceInfo getServiceInfoByServiceCode(String serviceCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("serviceCode", serviceCode);
        params.put("status", "0");
        List<ServiceInfo> serviceInfoList = listServiceInfosByParams(params);
        if (serviceInfoList != null && serviceInfoList.size() > 0) {
            return serviceInfoList.get(0);
        }
        return null;
    }

    /**
     * @Description 根据serviceModule查询ServiceInfo列表
     * @auther: FENG.yanmin
     * @UpadteDate: 2019/1/28 16:59
     */
    public List<ServiceInfo> listServiceInfosByServiceModule(String serviceModule) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("serviceModule", serviceModule);
        params.put("status", "0");
        List<ServiceInfo> serviceInfoList = listServiceInfosByParams(params);
        return serviceInfoList;
    }

}
