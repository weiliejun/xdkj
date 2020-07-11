package com.xdkj.common.model.servicecalllog.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.servicecalllog.bean.ServiceCallLog;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ServiceCallLogDao extends AbstractBaseDao {

    public ServiceCallLog addServiceCallLog(ServiceCallLog serviceCallLog) {
        insert("servicecalllog.addServiceCallLog", serviceCallLog);
        return serviceCallLog;
    }

    public void updateServiceCallLog(ServiceCallLog serviceCallLog) {
        update("servicecalllog.updateServiceCallLog", serviceCallLog);
    }

    public void updateServiceCallLogByOrdId(String ordId, String respContent) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ordId", ordId);
        params.put("respContent", respContent);
        params.put("respTime", new Date());
        update("servicecalllog.updateServiceCallLogByOrdId", params);
    }

    public ServiceCallLog getServiceCallLogById(Integer id) {
        return (ServiceCallLog) queryForObject("servicecalllog.getServiceCallLogById", id);
    }

    public ServiceCallLog getServiceCallLogByOrdId(String ordId) {
        return (ServiceCallLog) queryForObject("servicecalllog.getServiceCallLogByOrdId", ordId);
    }
}
