package com.xdkj.pc.service.userManage;

import com.xdkj.common.model.userSmsTemplateApplication.bean.UserSmsTemplateApplication;
import com.xdkj.common.model.userSmsTemplateApplication.dao.UserSmsTemplateApplicationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userSmsTemplateApplicationService")
public class UserSmsTemplateApplicationServiceImpl implements UserSmsTemplateApplicationService {

    @Autowired
    private UserSmsTemplateApplicationDao userSmsTemplateApplicationDao;

    public void addUserSmsTemplateApplication(UserSmsTemplateApplication userSmsTemplateApplication) {
        userSmsTemplateApplicationDao.addUserSmsTemplateApplication(userSmsTemplateApplication);
    }

    public UserSmsTemplateApplication getUserSmsTemplateApplicationById(Integer id) {
        return userSmsTemplateApplicationDao.getUserSmsTemplateApplicationById(id);
    }

    public void updateUserSmsTemplateApplication(UserSmsTemplateApplication userSmsTemplateApplication) {
        userSmsTemplateApplicationDao.updateUserSmsTemplateApplication(userSmsTemplateApplication);
    }

    public List<UserSmsTemplateApplication> listUserSmsTemplateApplicationsByParams(Map<String, Object> params) {
        return userSmsTemplateApplicationDao.listUserSmsTemplateApplicationsByParams(params);
    }

    public int countUserSmsTemplateApplicationsByParams(Map<String, Object> params) {
        return userSmsTemplateApplicationDao.countUserSmsTemplateApplicationsByParams(params);
    }
}
