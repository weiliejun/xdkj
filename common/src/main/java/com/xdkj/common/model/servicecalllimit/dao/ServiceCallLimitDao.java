package com.xdkj.common.model.servicecalllimit.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.servicecalllimit.bean.ServiceCallLimit;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ServiceCallLimitDao extends AbstractBaseDao {

    public ServiceCallLimit addServiceCallLimit(ServiceCallLimit serviceCallLimit) {
        insert("servicecalllimit.addServiceCallLimit", serviceCallLimit);
        return serviceCallLimit;
    }

    public void updateServiceCallLimit(ServiceCallLimit serviceCallLimit) {
        update("servicecalllimit.updateServiceCallLimit", serviceCallLimit);
    }

    public ServiceCallLimit getServiceCallLimitById(Integer id) {
        return (ServiceCallLimit) queryForObject("servicecalllimit.getServiceCallLimitById", id);
    }

    /**
     * @Description 根据动态参数查询ServiceCallLimit
     * @auther: zhangkele
     * @UpadteDate: 2019/1/23 14:35
     */
    public List<ServiceCallLimit> listServiceCallLimitsByParams(Map<String, Object> params) {
        return (List<ServiceCallLimit>) queryForList("servicecalllimit.listServiceCallLimitsByParams", params);
    }

    /**
     * @Description 根据serviceCode 和 menuType 查询serviceCallLimit
     * @auther: zhangkele
     * @UpadteDate: 2019/1/23 14:37
     */
    public ServiceCallLimit getServiceCallLimitByServiceCodeAndMenuType(String serviceCode, String menuType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("serviceCode", serviceCode);
        params.put("menuType", menuType);
        List<ServiceCallLimit> serviceCallLimits = listServiceCallLimitsByParams(params);
        if (serviceCallLimits != null && serviceCallLimits.size() > 0) {
            return serviceCallLimits.get(0);
        }
        return null;
    }

}
