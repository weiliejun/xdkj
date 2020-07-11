package com.xdkj.admin.system.service;


import com.xdkj.common.model.serviceinfo.bean.ServiceInfo;
import com.xdkj.common.model.serviceinfo.dao.ServiceInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("serviceInfoService")
public class ServiceInfoServiceImpl implements ServiceInfoService {

    @Autowired
    private ServiceInfoDao serviceInfoDao;

    public void addServiceInfo(ServiceInfo serviceInfo) {
        serviceInfoDao.addServiceInfo(serviceInfo);
    }

    public void updateServiceInfo(ServiceInfo serviceInfo) {
        serviceInfoDao.updateServiceInfo(serviceInfo);
    }

    public ServiceInfo getServiceInfoById(Integer id) {
        return serviceInfoDao.getServiceInfoById(id);
    }

    public List<ServiceInfo> listServiceInfosByParams(Map<String, Object> params) {
        return serviceInfoDao.listServiceInfosByParams(params);
    }
}
