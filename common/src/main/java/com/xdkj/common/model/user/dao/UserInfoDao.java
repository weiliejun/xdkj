package com.xdkj.common.model.user.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.redis.util.RedisEnum;
import com.xdkj.common.redis.util.RedisUtil;
import com.xdkj.common.web.SessionUser;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserInfoDao extends AbstractBaseDao {
    /**
     * @Description: 新增用户信息
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 16:08
     */
    public void addUserInfo(UserInfo userInfo) {
        insert("userinfo.addUserInfo", userInfo);
    }

    /**
     * @Description: 修改用户信息
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 16:08
     */
    public void updateUserInfo(UserInfo userInfo) {
        //刷新redis中SessionUserInfo信息
        refreshSessionUserInfo(userInfo.getId());
        update("userinfo.updateUserInfo", userInfo);
    }

    /**
     * @Description: 修改用户账户金额
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 16:08
     */
    public void updateUserAccountBalance(Integer userId, BigDecimal blance) {
        UserInfo userTemp = new UserInfo();
        userTemp.setId(userId);
        userTemp.setUserAccountBalance(blance);
        updateUserInfo(userTemp);
    }

    /**
     * @Description 刷新redis中SessionUserInfo信息
     * @auther: zhangkele
     * @UpadteDate: 2019/1/29 11:11
     */
    public void refreshSessionUserInfo(Integer userId) {
        UserInfo userTemp = getUserInfoById(userId);

        SessionUser sessionUser = null;
        String sid = cacheDao.get(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{String.valueOf(userId)}));
        if (sid != null && !"".equalsIgnoreCase(sid)) {
            String key = RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid});
            sessionUser = (SessionUser) cacheDao.getObject(key);
        }
        if (sessionUser != null) {
            sessionUser.setUserInfo(userTemp);
            String key = RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid});
            cacheDao.set(key, sessionUser);
        }
    }


    /**
     * @Description: 根据id获取用户信息
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 16:08
     */
    public UserInfo getUserInfoById(Integer id) {
        return (UserInfo) queryForObject("userinfo.getUserInfoById", id);
    }

    /**
     * @Description: 根据动态参数获取用户列表
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 16:08
     */
    public List<UserInfo> listUserInfosByParams(Map<String, Object> params) {
        return (List<UserInfo>) queryForList("userinfo.listUserInfosByParams", params);
    }

    /**
     * @Description: 根据用户名和密码获取用户信息
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 16:08
     */
    public UserInfo getUserInfoByUserNameAndPassword(String userName, String password) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userName", userName);
        params.put("password", password);
        return (UserInfo) queryForObject("userinfo.getUserInfoByUserNameAndPassword", params);
    }

    /**
     * @Description: 根据昵称或手机号获取用户信息
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 16:08
     */
    public UserInfo getUserInfoByNickNameOrMobile(String userName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userName", userName);
        return (UserInfo) queryForObject("userinfo.getUserInfoByNickNameOrMobile", params);
    }

    /**
     * @Description: 根据昵称获取用户信息
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 16:08
     */
    public UserInfo getUserInfoByNickName(String nickName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("nickName", nickName);
        List<UserInfo> userInfoList = listUserInfosByParams(params);
        if (userInfoList != null && userInfoList.size() > 0) {
            return userInfoList.get(0);
        }
        return null;
    }

    /**
     * @Description: 根据手机号获取用户信息
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 16:08
     */
    public UserInfo getUserInfoByMobile(String mobile) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", mobile);
        List<UserInfo> userInfoList = listUserInfosByParams(params);
        if (userInfoList != null && userInfoList.size() > 0) {
            return userInfoList.get(0);
        }
        return null;
    }

    public UserInfo getUserInfoByAppKey(String appKey) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appKey", appKey);
        List<UserInfo> userInfoList = listUserInfosByParams(params);
        if (userInfoList != null && userInfoList.size() > 0) {
            return userInfoList.get(0);
        }
        return null;
    }

}
