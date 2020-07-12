package com.xdkj.admin.business.deviceInfo.service;


import com.xdkj.common.model.deviceInfo.bean.DeviceInfo;

import java.util.List;
import java.util.Map;

public interface DeviceInfoService {
    DeviceInfo addDeviceInfo(DeviceInfo DeviceInfo);

    DeviceInfo getDeviceInfoById(String id);

    DeviceInfo getDeviceInfoByName(String name);

    void updateDeviceInfo(DeviceInfo DeviceInfo);

    List<DeviceInfo> listDeviceInfoByParams(Map<String, Object> params);

    List<DeviceInfo> listDeviceInfoByModel(DeviceInfo DeviceInfo);

}