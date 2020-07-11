package com.xdkj.pc.web.base;

import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.redis.service.CacheService;
import com.xdkj.common.web.SessionUser;
import com.xdkj.pc.service.userManage.UserInfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: controller基类
 */
public abstract class AbstractBaseController {
    private static final Log logger = LogFactory.getLog(AbstractBaseController.class);

    @Autowired
    protected CacheService cacheService;

    @Autowired
    protected UserInfoService userInfoService;

    /**
     * 根据sessionId获取CurrentUser
     */
    protected SessionUser getSessionUserBySid(HttpServletRequest request) {
        String sid = request.getSession().getId();
        return userInfoService.getSessionUserBySid(sid);
    }

    /**
     * 根据sessionId获取UserInfo
     */
    protected UserInfo getUserInfoBySid(HttpServletRequest request) {
        SessionUser sessionUser = getSessionUserBySid(request);
        if (sessionUser != null) {
            return sessionUser.getUserInfo();
        }
        return null;
    }

    /**
     * 根据userId获取CurrentUser
     */
    protected SessionUser getSessionUserByUid(Integer uid) {
        return userInfoService.getSessionUserByUid(uid);
    }


}
