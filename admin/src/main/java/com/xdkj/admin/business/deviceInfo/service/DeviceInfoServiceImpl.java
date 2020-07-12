package com.xdkj.admin.business.deviceInfo.service;

import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.deviceInfo.bean.DeviceInfo;
import com.xdkj.common.model.deviceInfo.dao.DeviceInfoMapper;
import com.xdkj.common.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("DeviceInfoService")
public class DeviceInfoServiceImpl implements DeviceInfoService {

    @Autowired
    private DeviceInfoMapper dlsInfoMapper;

    /**
     * 添加人员信息管理
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 16:41
     * @Param
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceInfo addDeviceInfo(DeviceInfo DeviceInfo) {
        // Id
        DeviceInfo.setId(RandomUtil.getSerialNumber());
        // 数据有效性 0 无效 1 有效
        DeviceInfo.setDataStatus(GlobalConstant.DATA_VALID);
        return dlsInfoMapper.addDeviceInfo(DeviceInfo);
    }

    /**
     * 获取人员信息管理
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 16:42
     * @Param
     **/
    @Override
    public DeviceInfo getDeviceInfoById(String id) {
        return dlsInfoMapper.selectDeviceInfoById(id);
    }

    @Override
    public DeviceInfo getDeviceInfoByName(String name) {
        return dlsInfoMapper.selectDeviceInfoByName(name);
    }

    /**
     * 修改人员信息管理
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 16:43
     * @Param
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceInfo(DeviceInfo DeviceInfo) {
        dlsInfoMapper.updateDeviceInfoById(DeviceInfo);
    }

    /**
     * 根据动态参数获取人员信息管理列表
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 17:18
     * @Param
     **/
    @Override
    public List<DeviceInfo> listDeviceInfoByParams(Map<String, Object> params) {
        return dlsInfoMapper.listDeviceInfoByParams(params);
    }

    /**
     * 根据人员信息管理pojo获取人员信息管理列表
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 17:18
     * @Param
     **/
    @Override
    public List<DeviceInfo> listDeviceInfoByModel(DeviceInfo DeviceInfo) {
        // 获取时默认获取有效数据
        DeviceInfo.setDataStatus(GlobalConstant.DATA_VALID);
        return dlsInfoMapper.selectDeviceInfoList(DeviceInfo);
    }

}
