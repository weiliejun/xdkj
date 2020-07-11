package com.xdkj.common.model.userExpense.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.userExpense.bean.UserExpense;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class UserExpenseDao extends AbstractBaseDao {

    /**
     * @Description 新增用户消费记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:13
     */
    public void addUserExpense(UserExpense userExpense) {
        insert("userExpense.addUserExpense", userExpense);
    }

    /**
     * @Description 修改用户消费记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:14
     */
    public void updateUserExpense(UserExpense userExpense) {
        update("userExpense.updateUserExpense", userExpense);
    }

    /**
     * @Description 通过appKey修改用户消费记录表状态
     * @auther: zhangkele
     * @UpadteDate: 2019/1/18 10:14
     */
    public void updateUserExpenseStatusByAppKey(String appKey, String status) {
        UserExpense userExpenseTemp = new UserExpense();
        userExpenseTemp.setAppkey(appKey);
        userExpenseTemp.setStatus(status);
        userExpenseTemp.setUpdateTime(new Date());
        update("userExpense.updateUserExpenseStatusByAppKey", userExpenseTemp);
    }

    /**
     * @Description 根据id获取用户消费记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    public UserExpense getUserExpenseById(Integer id) {
        return (UserExpense) queryForObject("userExpense.getUserExpenseById", id);
    }

    /**
     * @Description 根据动态参数获取用户消费记录列表
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    public List<UserExpense> listUserExpensesByParams(Map<String, Object> params) {
        return (List<UserExpense>) queryForList("userExpense.listUserExpensesByParams", params);
    }

    /**
     * @Description 根据动态参数获取用户消费记录总条数
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    public Integer countUserExpenses(Map<String, Object> params) {
        return (Integer) queryForObject("userExpense.countUserExpenses", params);
    }

    /**
     * @Description 根据userId获取用户最近消费记录列表
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    public List<Map<String, Object>> listUserExpensesRecently(Integer userId) {
        return (List<Map<String, Object>>) queryForList("userExpense.listUserExpensesRecently", userId);
    }


    public List<Map<String, Object>> listUserExpensesMapByParams(Map<String, Object> params) {
        return queryForList("userExpense.listUserExpensesMapByParams", params);
    }
}