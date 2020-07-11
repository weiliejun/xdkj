package com.xdkj.admin.web.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xdkj.admin.system.service.MonitorService;
import com.xdkj.admin.system.service.UserInfoService;
import com.xdkj.common.constant.ApplicationSessionKeys;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.redis.service.CacheService;
import com.xdkj.common.util.*;
import com.xdkj.common.web.SessionUser;
import com.xdkj.common.web.base.MyHttpServletRequest;
import com.xdkj.common.web.bean.CurrentManager;
import com.xdkj.common.web.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
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
    protected MonitorService monitorService;
    @Autowired
    protected UserInfoService userInfoService;

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

    /**
     * 获取表单上传的文件
     * 配置文件中如果去除了springboot项目默认的多部件解析器 依赖commons-fileUpload包
     * 使用springMvc中的CommonsMultipartResolver进行解析
     * 将传递HttpServletRequest或者ShiroHttpServletRequest解析成为MultipartHttpServletRequest并返回表单中所有的MultipartFile
     *
     * @return
     * @Author 李洪斌
     * @Description //TODO
     * @Date 2019/7/23 19:09
     * @Param
     **/
    public Map<String, MultipartFile> getFormMultipartFile(HttpServletRequest request) {
        //将当前上下文初始化给  CommonsMultipartResolver （多部分解析器）
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
        //检查form中是否有enctype="multipart/form-data"
        if (resolver.isMultipart(request)) {
            return multipartRequest.getFileMap();
        }
        return null;
    }

    /**
     * @Description 从session中获取用户
     * @auther: zhangkele
     * @UpadteDate: 2019/3/4 15:05
     */
    protected SysManager getSessionSysManager() {
        Subject subject = SecurityUtils.getSubject();
        subject.getSession();
        CurrentManager currentManager = (CurrentManager) subject.getSession().getAttribute(ApplicationSessionKeys.CURRENT_USER);
        if (currentManager != null) {
            return currentManager.getSysManager();
        }
        return null;
    }

    /**
     * ajax保存失败时，重置token
     */
    protected void resetSessionToken(Map<String, Object> resultMap) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String token = WebUtil.generateToken(session.getId().toString());
        session.setAttribute("token", token);
        resultMap.put("token", token);
    }

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
     * @param request
     * @return
     * @description 获取表单请求信息：解密、验证
     * @version v1.0
     * @author 代树理
     * @update 2018年4月12日 下午1:48:57
     */
    protected Map<String, Object> getVerificationData(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();

        boolean verifySign = false;
        String requestKey = "";
        String sourceType = request.getHeader("Resource-Type");
        if (StringHelper.isNotBlank(sourceType) && "explore".equals(sourceType)) {
            // 使用字符串形式读取body内容，只能读取一次
            MyHttpServletRequest requestWrapper = null;
            if (request instanceof HttpServletRequest) {
                requestWrapper = new MyHttpServletRequest(request);
                requestKey = requestWrapper.getRequestKey();
            }
        } else {
            Map parameterMap = request.getParameterMap();
            Enumeration enu = enu = request.getParameterNames();
            while (enu.hasMoreElements()) {
                String paraName = (String) enu.nextElement();
                String paraValue = request.getParameter(paraName).trim();
                params.put(paraName, paraValue);
            }

            requestKey = params.get("requestKey").toString();
        }

        params.clear();

        // 解密
        String decryptParam;
        try {
            if (StringUtils.isBlank(requestKey)) {
                throw new IllegalArgumentException("parameter con't be null!");
            }
            decryptParam = DES3.decrypt(requestKey);
            JSONObject paramJson = JSON.parseObject(decryptParam);
            JSONObject data = paramJson.getJSONObject("data");
            String check = paramJson.getString("check");

            if (data == null || check == null) {
                throw new IllegalArgumentException("parameter must include field data, check! ");
            }
            String md5 = MD5Util.MD5(JSON.toJSONString(data));
            if (md5.equals(check)) {
                verifySign = true;
            }
            params.put("verifySign", verifySign);
            params.put("data", data);
            params.put("signError", DES3.encrypt(JsonUtil.getResultJson(GlobalConstant.ERROR, "签名错误")));
        } catch (Exception e) {
            try {
                params.put("verifySign", false);
            } catch (Exception e1) {
                logger.error("FAIL encrypt error! ", e1);
            }
        }
        return params;
    }

    /**
     * 防止sql注入
     */
    public String filterQuery(String str) {
        return str.replaceAll(".*([';]+|(--)+).*", " ");
    }

    /**
     * 记录业务日志
     */
    protected void saveBusinessLog(String functionModule, String functionDescription, Object data) {
        logger.info("进入记录业务日志处理");
        try {
            Subject subject = SecurityUtils.getSubject();
            CurrentManager currentManager = (CurrentManager) subject.getSession().getAttribute(ApplicationSessionKeys.CURRENT_USER);
            String operationData = JSON.toJSONString(data);
            logger.info("调试信息：");
            if (operationData != null && operationData.length() > 1300) {
                operationData = operationData.substring(0, 1300);
            }
            String message = "操作人员:" + currentManager.getSysManager().getName() + " 操作时间:" + DateHelper.getYMDHMSFormatDate(new Date()) + " 操作功能模块：" + functionModule + " 操作功能描述:" + functionDescription + " 操作数据:" + operationData;
            logger.info(message);
            monitorService.saveBusinessLog(currentManager, functionModule, functionDescription, operationData);
        } catch (Exception e) {
            logger.error(e);
        }
        logger.info("记录业务日志处理结束！");
    }
}
