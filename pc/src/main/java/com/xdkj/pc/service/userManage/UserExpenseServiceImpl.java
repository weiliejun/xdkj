package com.xdkj.pc.service.userManage;

import com.xdkj.common.model.userExpense.bean.UserExpense;
import com.xdkj.common.model.userExpense.dao.UserExpenseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userExpenseService")
public class UserExpenseServiceImpl implements UserExpenseService {

    @Autowired
    private UserExpenseDao userExpenseDao;

    public void addUserExpense(UserExpense userExpense) {
        userExpenseDao.addUserExpense(userExpense);
    }

    public void updateUserExpense(UserExpense userExpense) {
        userExpenseDao.updateUserExpense(userExpense);
    }

    public UserExpense getUserExpenseById(Integer id) {
        return userExpenseDao.getUserExpenseById(id);
    }

    public List<UserExpense> listUserExpensesByParams(Map<String, Object> params) {
        return userExpenseDao.listUserExpensesByParams(params);
    }

    public Integer countUserExpenses(Map<String, Object> params) {
        return userExpenseDao.countUserExpenses(params);
    }

    public List<Map<String, Object>> listUserExpensesRecently(Integer userId) {
        return userExpenseDao.listUserExpensesRecently(userId);
    }
}
