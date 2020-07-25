package com.xdkj.pc.web.base;

import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.redis.service.CacheService;
import com.xdkj.common.util.StringHelper;
import com.xdkj.common.web.SessionUser;
import com.xdkj.pc.service.userManage.UserInfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * @Description 表单查询条记忆功能
     * @auther: zhangkele
     * @UpadteDate: 2019/2/22 9:05
     */
    protected Map<String, Object> formQueryRemember(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        //查询路径
        String queryURI = request.getRequestURI();
        HttpSession session = request.getSession();

        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            String paraValue = request.getParameter(paraName).trim();
            if (StringHelper.isNotEmpty(paraValue)) {
                params.put(paraName, paraValue);
            } else {
                params.remove(paraName);
            }
        }
        //分页信息默认值设置
        String pageSize = request.getParameter("pageSize") == null ? "5" : request.getParameter("pageSize");
        String currentPage = request.getParameter("currentPage") == null ? "1" : request.getParameter("currentPage");
        if (params.get("pageSize") == null) {
            params.put("pageSize", pageSize);
        }
        if (params.get("currentPage") == null) {
            params.put("currentPage", currentPage);
        }
        session.setAttribute(queryURI, params);
        return params;
    }

    protected Map<String, Object> getQureyParams(Map<String, Object> requestParams) {
        Map<String, Object> params = new HashMap<String, Object>();
        //移除分页信息
        requestParams.remove("pageSize");
        requestParams.remove("currentPage");

        params.putAll(requestParams);
        return params;
    }
}
