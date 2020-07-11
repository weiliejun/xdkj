package com.xdkj.admin.system.service;

import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.web.SessionUser;

import java.util.List;
import java.util.Map;

public interface UserInfoService {

    void addUserInfo(UserInfo userInfo);

    void updateUserInfo(UserInfo userInfo);

    UserInfo getUserById(Integer id);

    UserInfo getUserByMobile(String mobile);

    UserInfo getUserInfoByNickName(String nickName);

    /**
     * @Description 根据appKey查询用户
     * @auther: zhangkele
     * @UpadteDate: 2019/1/24 16:41
     */
    UserInfo getUserInfoByAppKey(String appKey);

    /**
     * 注册信息-校验（发送短信验证码按钮、注册按钮）（公共方法）
     *
     * @param mobile
     * @param nickName
     * @param validateCode   图形码
     * @param exitVerifyCode session保存的图形码
     * @Author Feng.yanmin
     */
    Map<String, String> userRegisterVerify(String mobile, String nickName, String validateCode, String exitVerifyCode);

    /**
     * 提交注册信息
     *
     * @param mobile
     * @param password
     * @param nickName
     * @Author Feng.yanmin
     */
    Map<String, String> userRegisterSubmit(String mobile, String password, String nickName);

    /**
     * @Description: 登录逻辑
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 18:26
     */
    Map<String, String> login(String userName, String password, String ip, String sid, String exitVerifyCode, String validateCode);

    /**
     * @Description: 保存session信息
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 18:26
     */
    void saveSession(String sid, SessionUser sessionUser);

    /**
     * @Description: 登出
     * @Author: zhangkele
     * @UpdateDate: 2019/1/17 18:26
     */
    void loginOut(String sid, Integer uid);

    /**
     * 是指定的用户下线
     */
    void loginOut(Integer uid);

    /**
     * 指定的用户下线(根据sessionid)
     */
    void loginOut(String sid);

    /**
     * 根据sessionId获取CurrentUser
     */
    SessionUser getSessionUserBySid(String sid);

    /**
     * 根据userId获取CurrentUser
     */
    SessionUser getSessionUserByUid(Integer uid);

    /**
     * 根据userId获取修改CurrentUser
     */
    void eidtSessionUserByUid(Integer uid, SessionUser sessionUser);

    /**
     * 根据sessionId修改CurrentUser 返回修改后的CurrentUser
     */
    void eidtSessionUserBySessionId(String sid, SessionUser sessionUser);

    /**
     * 指定用户是否在线
     */
    boolean isOnline(Integer uid);

    /**
     * 清除用户密码错误次数
     */
    void clearPasswordErrorCount(String userName);

    /**
     * 忘记密码校验保存
     */
    Map<String, String> checkAndSavePassword(String mobile, String password);

    /**
     * @Description 修改AppSecret
     * @auther: xsp
     * @UpadteDate: 2019/1/22 17:21
     */
    Map<String, String> updateUserAppSecret(Integer userId, String sid);


    /**
     * @Description 有效用户列表
     * @auther: cyp
     * @UpadteDate: 2019-02-28 10:55
     */
    List<UserInfo> listUserInfosByParams(Map<String, Object> params);

}
