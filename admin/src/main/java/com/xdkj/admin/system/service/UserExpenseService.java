package com.xdkj.admin.system.service;

import com.xdkj.common.model.userExpense.bean.UserExpense;

import java.util.List;
import java.util.Map;

public interface UserExpenseService {
    /**
     * @Description 新增用户消费记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:13
     */
    void addUserExpense(UserExpense userExpense);

    /**
     * @Description 修改用户消费记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:14
     */
    void updateUserExpense(UserExpense userExpense);

    /**
     * @Description 根据id获取用户消费记录
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    UserExpense getUserExpenseById(Integer id);

    /**
     * @Description 根据动态参数获取用户消费记录列表
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    List<UserExpense> listUserExpensesByParams(Map<String, Object> params);

    /**
     * @Description 根据动态参数获取用户消费记录列表
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    List<Map<String, Object>> listUserExpensesMapByParams(Map<String, Object> params);

    /**
     * @Description 根据动态参数获取用户消费记录总条数
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    Integer countUserExpenses(Map<String, Object> params);

    /**
     * @Description 根据userId获取用户最近消费记录列表
     * @auther: xsp
     * @UpadteDate: 2019/1/18 10:17
     */
    List<Map<String, Object>> listUserExpensesRecently(Integer userId);
}