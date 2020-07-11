package com.xdkj.pc.service.userManage;

import com.xdkj.common.constant.MessageBusiType;
import com.xdkj.common.constant.ResultCode;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.user.dao.UserInfoDao;
import com.xdkj.common.redis.service.CacheService;
import com.xdkj.common.redis.util.RedisEnum;
import com.xdkj.common.redis.util.RedisUtil;
import com.xdkj.common.util.DateHelper;
import com.xdkj.common.util.MD5Util;
import com.xdkj.common.util.StringHelper;
import com.xdkj.common.util.UUIDUtil;
import com.xdkj.common.web.SessionUser;
import com.xdkj.pc.service.sysMessageManage.SysMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    // 密码错误次数失效时间(120分钟)
    private static final int PASSWORD_ERROR_TIME = 120 * 60;
    // session过期时间(30分钟)
    private static final int SESSION_INVALID_TIME = 30 * 60;

    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private CacheService cacheService;

    @Autowired
    private SysMessageService sysMessageService;

    public void addUserInfo(UserInfo userInfo) {
        userInfoDao.addUserInfo(userInfo);
    }

    public void updateUserInfo(UserInfo userInfo) {
        userInfoDao.updateUserInfo(userInfo);
    }

    public UserInfo getUserById(Integer id) {
        return userInfoDao.getUserInfoById(id);
    }

    public UserInfo getUserByMobile(String mobile) {
        return userInfoDao.getUserInfoByMobile(mobile);
    }

    public UserInfo getUserInfoByNickName(String nickName) {
        return userInfoDao.getUserInfoByNickName(nickName);
    }

    public UserInfo getUserInfoByAppKey(String appKey) {
        return userInfoDao.getUserInfoByAppKey(appKey);
    }

    /**
     * 注册-校验
     *
     * @Author Feng.yanmin
     * @UpdateDate: 2019/1/17 16:05
     */
    public Map<String, String> userRegisterVerify(String mobile, String nickName, String validateCode, String exitValidateCode) {
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            // 校验1：验证昵称是否存在
            UserInfo checkNickName = userInfoDao.getUserInfoByNickName(nickName);
            if (checkNickName != null) {
                resultMap.put("flag", ResultCode.REGISTOR_USER_IS_EXIST.code());
                resultMap.put("msg", ResultCode.REGISTOR_USER_IS_EXIST.message());
                return resultMap;
            }
            // 校验2：验证手机号是否存在
            UserInfo checkMoblie = userInfoDao.getUserInfoByMobile(mobile);
            if (checkMoblie != null) {
                resultMap.put("flag", ResultCode.REGISTOR_MOBILE_IS_EXIST.code());
                resultMap.put("msg", ResultCode.REGISTOR_MOBILE_IS_EXIST.message());
                return resultMap;
            }
            // 校验3：图形验证码
            if (StringHelper.isEmpty(exitValidateCode) || !exitValidateCode.equalsIgnoreCase(validateCode)) {
                resultMap.put("flag", ResultCode.VALIDATE_CODE_ERROR.code());
                resultMap.put("msg", ResultCode.VALIDATE_CODE_ERROR.message());
                return resultMap;
            }

            resultMap.put("flag", ResultCode.SUCCESS.code());
            resultMap.put("msg", ResultCode.SUCCESS.message());
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("flag", ResultCode.PERMISSION_NO_ACCESS.code());
            resultMap.put("msg", ResultCode.PERMISSION_NO_ACCESS.message());
        }
        return resultMap;
    }

    /**
     * 注册-提交
     *
     * @Author Feng.yanmin
     * @UpdateDate: 2019/1/17 16:05
     */
    public Map<String, String> userRegisterSubmit(String mobile, String password, String nickName) {
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            // 1、添加数据
            UserInfo user = new UserInfo();
            user.setNickName(nickName);
            user.setPassword(MD5Util.MD5(password));
            user.setMobile(mobile);
            user.setAppkey(UUIDUtil.getUUID());
            user.setAppsecret(UUIDUtil.getUUID());
            user.setAuthenticationStatus("1"); //0-实名，1-未实名）
            user.setTerminal("pc"); //（0-pc,1-android, 2- ios）
            user.setDataStatus("0"); //（0-有效，1-无效）
            user.setCreateTime(new Date());
            userInfoDao.addUserInfo(user);
            // 2、发送消息通知
            UserInfo newUser = userInfoDao.getUserInfoByMobile(mobile);
            Map<String, String> params = new HashMap<String, String>();
            //params.put("date", DateHelper.getYMDFormatDate(new Date()));
            sysMessageService.sendWebSiteMessage(newUser.getId(), MessageBusiType.REGISTER, params);

            resultMap.put("flag", ResultCode.SUCCESS.code());
            resultMap.put("msg", ResultCode.SUCCESS.message());

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("flag", ResultCode.PERMISSION_NO_ACCESS.code());
            resultMap.put("msg", ResultCode.PERMISSION_NO_ACCESS.message());
        }
        return resultMap;
    }

    /**
     * @Description: 登录逻辑
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 16:05
     */
    public Map<String, String> login(String userName, String password, String ip, String sid, String exitVerifyCode, String validateCode) {
        Map<String, String> result = new HashMap<String, String>();

        if (exitVerifyCode == null || !exitVerifyCode.equalsIgnoreCase(validateCode)) {
            result.put("flag", "false");
            result.put("message", "验证码错误！");
            return result;
        }

        //参数非空校验
        if (StringHelper.isEmpty(userName) || StringHelper.isEmpty(password)) {
            result.put("flag", "false");
            result.put("message", "用户名或密码不能为空，请填写！");
            return result;
        }
        UserInfo userInfo = userInfoDao.getUserInfoByNickNameOrMobile(userName.trim());
        //账户存在性校验
        if (userInfo == null) {
            result.put("flag", "false");
            result.put("message", "您输入的账户不存在，请重新输入！");
            return result;
        }
        //账户有效校验，0：有效；1无效（后期）
        if ("1".equals(userInfo.getDataStatus())) {
            result.put("flag", "false");
            result.put("message", "对不起，您的账户状态为 '无效' 状态，请联系网站！");
            return result;
        }

        //密码输错次数校验
        String passwordErrorCountKey = RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_PASSWORDERRORCOUNT, userName);
        Integer passwordErrorCount = (Integer) cacheService.getObject(passwordErrorCountKey);
        passwordErrorCount = passwordErrorCount == null ? 0 : passwordErrorCount;
        if (passwordErrorCount >= 3) {
            result.put("flag", "false");
            result.put("message", "密码连续输错三次，请两小时后重试！");
            return result;
        }

        //密码校验
        String passwordMd5 = MD5Util.MD5(password);
        if (!passwordMd5.equalsIgnoreCase(userInfo.getPassword())) {
            passwordErrorCount++;
            cacheService.set(passwordErrorCountKey, passwordErrorCount, PASSWORD_ERROR_TIME);
            result.put("flag", "false");
            result.put("message", "密码不正确，请重新输入！");
            return result;
        }

        /**
         * 踢除其它端登录
         */
        Integer userInfoId = userInfo.getId();
        if (isOnline(userInfoId)) {// 用户在线
            // 踢除其它端登录
            loginOut(userInfoId);
        }
        /**
         *  保存用户登录信息
         */
        SessionUser sessionUser = new SessionUser();
        sessionUser.setUserInfo(userInfo);
        sessionUser.setIp(ip);
        sessionUser.setSessionId(sid);
        saveSession(sid, sessionUser);
        /**
         *  清除密码错误
         */
        if (passwordErrorCount > 0) {
            clearPasswordErrorCount(userName);
        }

        result.put("flag", "true");
        result.put("message", "登陆成功！");
        return result;
    }

    public void saveSession(String sid, SessionUser sessionUser) {
        /**
         * redis 保存session信息时
         */
        UserInfo userInfo = sessionUser.getUserInfo();
        Integer uid = userInfo.getId();

        //根据sid查询原有的uid
        String oldUid = cacheService.get(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid, "uid"}));
        //判断key是否存在，若存在就删除
        if (oldUid != null && !"".equals(oldUid)) {
            cacheService.remove(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{oldUid}));
        }
        //删除原有的key
        cacheService.remove(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid}));
        cacheService.remove(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid, "uid"}));

        //以sid为key，存储sid和sessionUser的关系
        cacheService.set(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid}), sessionUser, SESSION_INVALID_TIME);

        //以sid为key，存储sid和uid的关系
        cacheService.set(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid, "uid"}), String.valueOf(uid), SESSION_INVALID_TIME);

        //以uid为key,存储uid和sid的关系
        cacheService.set(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{String.valueOf(uid)}), sid, SESSION_INVALID_TIME);
    }

    public void loginOut(String sid, Integer uid) {
        //根据sid查询原有的uid
        String oldUid = cacheService.get(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid, "uid"}));
        //判断key是否存在，若存在就删除
        if (oldUid != null && !"".equals(oldUid)) {
            cacheService.remove(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{oldUid}));
        }
        cacheService.remove(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid}));
        cacheService.remove(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{String.valueOf(uid)}));
    }


    public void loginOut(Integer uid) {
        String sid = cacheService.get(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{String.valueOf(uid)}));
        if (sid != null && !"".equalsIgnoreCase(sid)) {
            //根据sid查询原有的uid
            String oldUid = cacheService.get(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid, "uid"}));
            //判断key是否存在，若存在就删除
            if (oldUid != null && !"".equals(oldUid)) {
                cacheService.remove(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{oldUid}));
            }
            cacheService.remove(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid}));
            cacheService.remove(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{String.valueOf(uid)}));
        }
    }

    public void loginOut(String sid) {
        SessionUser sessionUser = getSessionUserBySid(sid);
        if (sessionUser != null) {
            UserInfo userInfo = sessionUser.getUserInfo();
            Integer uid = userInfo.getId();
            cacheService.remove(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid}));
            cacheService.remove(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{String.valueOf(uid)}));
        }
    }

    public SessionUser getSessionUserBySid(String sid) {
        String key = RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid});
        SessionUser sessionUser = (SessionUser) cacheService.getObject(key);
        if (sessionUser != null) {//重置有效时间
            //以sid为key，存储sid和sessionUser的关系
            cacheService.expire(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid}), SESSION_INVALID_TIME);

            cacheService.expire(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid, "uid"}), SESSION_INVALID_TIME);

            String uid = cacheService.get(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid, "uid"}));
            cacheService.expire(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{uid}), SESSION_INVALID_TIME);
        }
        return sessionUser;
    }


    public SessionUser getSessionUserByUid(Integer uid) {
        String sid = cacheService.get(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{String.valueOf(uid)}));
        if (sid != null && !"".equalsIgnoreCase(sid)) {
            String key = RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid});
            return (SessionUser) cacheService.getObject(key);
        }
        return null;
    }

    public void eidtSessionUserByUid(Integer uid, SessionUser sessionUser) {
        String sid = cacheService.get(RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{String.valueOf(uid)}));
        if (sid != null && !"".equalsIgnoreCase(sid)) {
            String key = RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid});
            cacheService.set(key, sessionUser, SESSION_INVALID_TIME);
        }
    }

    public void eidtSessionUserBySessionId(String sid, SessionUser sessionUser) {
        String key = RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{sid});
        cacheService.set(key, sessionUser, SESSION_INVALID_TIME);
    }

    public boolean isOnline(Integer uid) {
        String key = RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_SESSION, new String[]{String.valueOf(uid)});
        return cacheService.hasKey(key);
    }

    public void clearPasswordErrorCount(String userName) {
        String passwordErrorCountKey = RedisUtil.keyBuilder(RedisEnum.USER_LOGIN_PASSWORDERRORCOUNT, userName);
        cacheService.remove(passwordErrorCountKey);
    }

    /**
     * @Description 忘记密码校验保存
     * @auther: cyp
     * @UpadteDate: 2019-01-22 16:07
     */
    public Map<String, String> checkAndSavePassword(String mobile, String password) {
        Map<String, String> result = new HashMap<String, String>();
        if (mobile == null) {
            result.put("result", "false");
            result.put("flag", ResultCode.FORGOT_PASSWORD_MOBILE_NOT_EXIST.code());
            result.put("message", ResultCode.FORGOT_PASSWORD_MOBILE_NOT_EXIST.message());
            return result;
        }
        if (password == null) {
            result.put("result", "false");
            result.put("flag", ResultCode.FORGOT_PASSWORD_NOT_EXIST.code());
            result.put("message", ResultCode.FORGOT_PASSWORD_NOT_EXIST.message());
            return result;
        }
        UserInfo userInfo = getUserByMobile(mobile);
        if (userInfo == null) {
            result.put("result", "false");
            result.put("flag", ResultCode.FORGOT_PASSWORD_SMS_CODE_ERR.code());
            result.put("message", ResultCode.FORGOT_PASSWORD_SMS_CODE_ERR.message());
            return result;
        }
        userInfo.setPassword(MD5Util.MD5(password));
        userInfo.setUpdateTime(new Date());
        updateUserInfo(userInfo);
        result.put("result", "true");
        // 登录密码找回成功，清空密码输错次数
        clearPasswordErrorCount(mobile);
        return result;
    }

    public Map<String, String> updateUserAppSecret(Integer userId, String sid) {
        Map<String, String> result = new HashMap<String, String>();

        // 1、修改数据库AppSecret
        UserInfo userInfoTmp = new UserInfo();
        userInfoTmp.setId(userId);
        userInfoTmp.setAppsecret(UUIDUtil.getUUID());
        userInfoTmp.setUpdateTime(new Date());
        userInfoDao.updateUserInfo(userInfoTmp);
        // 2、更新redis中AppSecret
        SessionUser sessionUser = new SessionUser();
        UserInfo userInfo = userInfoDao.getUserInfoById(userId);
        sessionUser.setUserInfo(userInfo);
        sessionUser.setSessionId(sid);
        eidtSessionUserByUid(userId, sessionUser);
        // 3、发送消息通知
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", DateHelper.getYMDFormatDate(new Date()));
        sysMessageService.sendWebSiteMessage(userInfo.getId(), MessageBusiType.RESET_APPSECRET, params);

        result.put("appSecret", userInfo.getAppsecret());
        result.put("flag", "true");
        return result;
    }

    public Map<String, Object> updatePassword(Integer userId, String newPassword) {
        Map<String, Object> result = new HashMap<String, Object>();

        // 1、修改用户密码
        UserInfo user = new UserInfo();
        user.setId(userId);
        user.setPassword(MD5Util.MD5(newPassword));
        user.setUpdateTime(new Date());
        userInfoDao.updateUserInfo(user);
        // 2、发送消息通知
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", DateHelper.getYMDFormatDate(new Date()));
        sysMessageService.sendWebSiteMessage(userId, MessageBusiType.EDIT_PASSWORD, params);

        result.put("flag", "true");
        result.put("msg", "修改成功");
        return result;
    }
}
