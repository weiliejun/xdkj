package com.xdkj.admin.business.user.service;

import com.xdkj.common.model.userTopup.bean.UserTopup;

import java.util.List;
import java.util.Map;

public interface UserTopupService {
    /**
     * @Description 新增用户充值记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:13
     */
    void addUserTopup(UserTopup userTopup);

    /**
     * @Description 修改用户充值记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:14
     */
    void updateUserTopup(UserTopup userTopup);

    /**
     * @Description 根据id获取用户充值记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    UserTopup getUserTopupById(Integer id);

    /**
     * @Description 根据动态参数获取用户充值记录列表
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    List<UserTopup> listUserTopupsByParams(Map<String, Object> params);

    /**
     * @Description 更新充值状态
     * @auther: xsp
     * @UpadteDate: 2019/3/6 13:03
     */
    Map<String, Object> updateTopUpStatus(Integer userTopUpId);
}