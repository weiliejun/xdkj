package com.xdkj.common.model.deviceInfo.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.deviceInfo.bean.DeviceInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DeviceInfoMapper extends AbstractBaseDao {
    /**
     * @Description 动态参数添加数据
     * @Author 魏列军
     * @UpdateDate 2020/5/15 10:12
     */
    public DeviceInfo addDeviceInfo(DeviceInfo record) {
        insert("DeviceInfoMapper.insertSelective", record);
        return record;
    }

    /**
     * @Description 根据Id查找人员信息
     * @Author 魏列军
     * @UpdateDate 2020/5/15 10:12
     */
    public DeviceInfo selectDeviceInfoById(String id) {
        return (DeviceInfo) queryForObject("DeviceInfoMapper.selectByPrimaryKey", id);
    }

    /**
     * @Description 根据name查找人员信息
     * @Author 魏列军
     * @UpdateDate 2020/5/15 10:12
     */
    public DeviceInfo selectDeviceInfoByName(String name) {
        return (DeviceInfo) queryForObject("DeviceInfoMapper.selectDeviceInfoByName", name);
    }

    /**
     * @Description 获取人员信息列表
     * @Author 魏列军
     * @UpdateDate 2020/5/15 10:12
     */
    public List<DeviceInfo> selectDeviceInfoList(DeviceInfo record) {
        return queryForList("DeviceInfoMapper.selectDeviceInfoList", record);
    }

    /**
     * @return
     * @Author 魏列军
     * @Description //TODO 根据动态参数获取人员列表
     * @Date 2020/5/15 15:04
     * @Param
     **/
    public List<DeviceInfo> listDeviceInfoByParams(Map<String, Object> params) {
        return queryForList("DeviceInfoMapper.listDeviceInfoByParams", params);
    }

    /**
     * @Description 根据id修改人员信息
     * @Author 魏列军
     * @UpdateDate 2020/5/15 10:12
     */
    public int updateDeviceInfoById(DeviceInfo record) {
        return update("DeviceInfoMapper.updateByPrimaryKeySelective", record);
    }

}