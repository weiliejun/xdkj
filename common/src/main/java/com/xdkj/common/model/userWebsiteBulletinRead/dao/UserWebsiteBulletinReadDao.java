package com.xdkj.common.model.userWebsiteBulletinRead.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.userWebsiteBulletinRead.bean.UserWebsiteBulletinRead;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserWebsiteBulletinReadDao extends AbstractBaseDao {

    public void addUserWebsiteBulletinRead(UserWebsiteBulletinRead userWebsiteBulletinRead) {
        insert("userWebsiteBulletinRead.insertUserWebsiteBulletinRead", userWebsiteBulletinRead);
    }

    public UserWebsiteBulletinRead getUserWebsiteBulletinReadById(Integer id) {
        return (UserWebsiteBulletinRead) queryForObject("userWebsiteBulletinRead.selectUserWebsiteBulletinReadById", id);
    }

    public UserWebsiteBulletinRead getUserWebsiteBulletinReadByBulletinId(Integer bulletinId, Integer userId) {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("bulletinId", bulletinId);
        params.put("userId", userId);
        return (UserWebsiteBulletinRead) queryForObject("userWebsiteBulletinRead.selectUserWebsiteBulletinReadByBulletinId", params);
    }

    public void updateUserWebsiteBulletinRead(UserWebsiteBulletinRead userWebsiteBulletinRead) {
        update("userWebsiteBulletinRead.updateUserWebsiteBulletinRead", userWebsiteBulletinRead);
    }

    public int countUserWebsiteBulletinReadByParam(Map<String, Object> param) {
        return (int) queryForObject("userWebsiteBulletinRead.countUserWebsiteBulletinReadByParam", param);
    }
}