package com.xdkj.common.model.userSmsTemplateApplication.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.userSmsTemplateApplication.bean.UserSmsTemplateApplication;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserSmsTemplateApplicationDao extends AbstractBaseDao {

    public void addUserSmsTemplateApplication(UserSmsTemplateApplication userSmsTemplateApplication) {
        insert("userSmsTemplateApplication.insertUserSmsTemplateApplication", userSmsTemplateApplication);
    }

    public UserSmsTemplateApplication getUserSmsTemplateApplicationById(Integer id) {
        return (UserSmsTemplateApplication) queryForObject("userSmsTemplateApplication.selectUserSmsTemplateApplicationById", id);

    }

    public void updateUserSmsTemplateApplication(UserSmsTemplateApplication userSmsTemplateApplication) {
        update("userSmsTemplateApplication.updateUserSmsTemplateApplication", userSmsTemplateApplication);
    }

    public List<UserSmsTemplateApplication> listUserSmsTemplateApplicationsByParams(Map<String, Object> params) {
        return queryForList("userSmsTemplateApplication.listUserSmsTemplateApplicationsByParams", params);
    }

    public int countUserSmsTemplateApplicationsByParams(Map<String, Object> params) {
        return (int) queryForObject("userSmsTemplateApplication.countUserSmsTemplateApplicationByParams", params);
    }

}