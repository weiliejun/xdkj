package com.xdkj.common.thirdparty.logger;


import com.alibaba.fastjson.JSON;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.servicecalllog.bean.ServiceCallLog;
import com.xdkj.common.model.servicecalllog.dao.ServiceCallLogDao;
import com.xdkj.common.util.StringHelper;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;


/**
 * @Description 第三方接口调用日志切面
 * @auther: zhangkele
 * @UpadteDate: 2019/1/23 10:20
 */
@Aspect
@Component
public class ThirdPartyCallLogAspect {
    private final Logger logger = Logger.getLogger(ThirdPartyCallLogAspect.class);

    @Autowired
    private ServiceCallLogDao serviceCallLogDao;

    @Pointcut("execution (* com.xdkj.common.thirdparty.service.ThirdPartyCallService.*(..))")
    public void pointCut() {
    }

    public void doBefore(JoinPoint jp) {
        logger.info("log Begining method: " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName());
    }


    @Around(value = "pointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (!ArrayUtils.isEmpty(args)) {
            Map<String, String> params = (Map<String, String>) args[0];
            String ordId = params.get("ordId");
            String service = params.get("service");
            if (logger.isDebugEnabled()) {
                logger.debug("thirdpartyService invoke,params:" + JSON.toJSONString(params));
            }
            //新增请求接口日志
            ServiceCallLog serviceCallLog = new ServiceCallLog();
            if (StringHelper.isNotEmpty(params.get("userId"))) {
                serviceCallLog.setUserId(Integer.valueOf(params.get("userId")));
                serviceCallLog.setAppkey(params.get("appKey"));
            }
            serviceCallLog.setOrdId(ordId);
            serviceCallLog.setServiceCode(service);
            //移除多余的参数
            params.remove("userId");
            params.remove("appKey");
            params.remove("ordId");
            params.remove("service");
            String reqeustContent = JSON.toJSONString(params);
            serviceCallLog.setRequestContent(reqeustContent);
            serviceCallLog.setRequestTime(new Date());
            serviceCallLog.setCreateTime(new Date());
            serviceCallLog.setDataStatus(GlobalConstant.DATA_VALID);
            serviceCallLogDao.addServiceCallLog(serviceCallLog);
            params.put("service", service);
            String respContent = (String) pjp.proceed(args);
            if (logger.isDebugEnabled()) {
                logger.debug("thirdpartyService return,respContent:" + respContent);
            }
            String respContentProcess = StringHelper.moreLengthOfDBProcess(respContent, 2000);
            serviceCallLogDao.updateServiceCallLogByOrdId(ordId, respContentProcess);
            return respContent;
        }
        return null;
    }


    public void doAfter(JoinPoint jp) {
        logger.info("log Ending method: " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName());
    }


    @AfterThrowing(value = "pointCut()", throwing = "e")
    public void doThrowing(JoinPoint jp, Throwable e) {
        String message = "invoke method: " + jp.getTarget().getClass().getName() +
                "." + jp.getSignature().getName() + " throw exception";
        logger.error(message, e);
    }
}