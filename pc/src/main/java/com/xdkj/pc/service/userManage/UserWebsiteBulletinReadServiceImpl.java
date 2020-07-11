package com.xdkj.pc.service.userManage;

import com.xdkj.common.model.userWebsiteBulletinRead.bean.UserWebsiteBulletinRead;
import com.xdkj.common.model.userWebsiteBulletinRead.dao.UserWebsiteBulletinReadDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("userWebsiteBulletinReadService")
public class UserWebsiteBulletinReadServiceImpl implements UserWebsiteBulletinReadService {
    @Autowired
    private UserWebsiteBulletinReadDao userWebsiteBulletinReadDao;

    public void addUserWebsiteBulletinRead(UserWebsiteBulletinRead userWebsiteBulletinRead) {
        userWebsiteBulletinReadDao.addUserWebsiteBulletinRead(userWebsiteBulletinRead);
    }

    public UserWebsiteBulletinRead getUserWebsiteBulletinReadById(Integer id) {
        return userWebsiteBulletinReadDao.getUserWebsiteBulletinReadById(id);
    }

    public UserWebsiteBulletinRead getUserWebsiteBulletinReadByBulletinId(Integer bulletinId, Integer userId) {
        return userWebsiteBulletinReadDao.getUserWebsiteBulletinReadByBulletinId(bulletinId, userId);
    }

    public void updateUserWebsiteBulletinRead(UserWebsiteBulletinRead userWebsiteBulletinRead) {
        userWebsiteBulletinReadDao.updateUserWebsiteBulletinRead(userWebsiteBulletinRead);

    }

    public int countUserWebsiteBulletinReadByParam(Map<String, Object> params) {
        return userWebsiteBulletinReadDao.countUserWebsiteBulletinReadByParam(params);
    }

}