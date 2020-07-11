package com.xdkj.common.model.userTopup.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.userTopup.bean.UserTopup;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class UserTopupDao extends AbstractBaseDao {

    /**
     * @Description 新增用户充值记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:13
     */
    public void addUserTopup(UserTopup userTopup) {
        insert("userTopup.addUserTopup", userTopup);
    }

    /**
     * @Description 修改用户充值记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:14
     */
    public void updateUserTopup(UserTopup userTopup) {
        update("userTopup.updateUserTopup", userTopup);
    }

    /**
     * @Description 根据id获取用户充值记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    public UserTopup getUserTopupById(Integer id) {
        return (UserTopup) queryForObject("userTopup.getUserTopupById", id);
    }

    /**
     * @Description 根据userId获取用户充值记录列表
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    public List<UserTopup> listUserTopupsByParams(Map<String, Object> params) {
        return (List<UserTopup>) queryForList("userTopup.listUserTopupsByParams", params);
    }

    /**
     * @Description 根据userId获取用户充值总金额
     * @auther: xsp
     * @UpadteDate: 2019/1/21 10:17
     */
    public BigDecimal countUserTopupTotalAmount(Integer userId) {
        return (BigDecimal) queryForObject("userTopup.countUserTopupTotalAmount", userId);
    }

}