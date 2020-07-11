package com.xdkj.common.model.userInfoExt.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.userInfoExt.bean.UserInfoExt;
import org.springframework.stereotype.Repository;

@Repository
public class UserInfoExtDao extends AbstractBaseDao {
    public int deleteByPrimaryKey(Integer userId) {
        return 0;
    }

    public int insert(UserInfoExt record) {
        return 0;
    }

    public int insertSelective(UserInfoExt record) {
        return 0;
    }

    public UserInfoExt selectByPrimaryKey(Integer userId) {
        return null;
    }

    public int updateByPrimaryKeySelective(UserInfoExt record) {
        return 0;
    }

    public int updateByPrimaryKey(UserInfoExt record) {
        return 0;
    }
}