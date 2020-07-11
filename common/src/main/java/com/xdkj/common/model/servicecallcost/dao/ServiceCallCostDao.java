package com.xdkj.common.model.servicecallcost.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.servicecallcost.bean.ServiceCallCost;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ServiceCallCostDao extends AbstractBaseDao {

    public ServiceCallCost addServiceCallCost(ServiceCallCost serviceCallCost) {
        insert("servicecallcost.addServiceCallCost", serviceCallCost);
        return serviceCallCost;
    }

    public void updateServiceCallCost(ServiceCallCost serviceCallCost) {
        update("servicecallcost.updateServiceCallCost", serviceCallCost);
    }

    public ServiceCallCost getServiceCallCostById(Integer id) {
        return (ServiceCallCost) queryForObject("servicecallcost.getServiceCallCostById", id);
    }

    /**
     * @Description 根据动态参数查询SServiceCallCost列表
     * @auther: zhangkele
     * @UpadteDate: 2019/1/23 14:35
     */
    public List<ServiceCallCost> listServiceCallCostsByParams(Map<String, Object> params) {
        return (List<ServiceCallCost>) queryForList("servicecallcost.listServiceCallCostsByParams", params);
    }

    /**
     * @Description 根据serviceCode 和 menuType 查询ServiceCallCost
     * @auther: zhangkele
     * @UpadteDate: 2019/1/23 14:37
     */
    public ServiceCallCost getServiceCallCostByServiceCodeAndMenuType(String serviceCode, String menuType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("serviceCode", serviceCode);
        params.put("menuType", menuType);
        List<ServiceCallCost> serviceCallCosts = listServiceCallCostsByParams(params);
        if (serviceCallCosts != null && serviceCallCosts.size() > 0) {
            return serviceCallCosts.get(0);
        }
        return null;
    }

}
