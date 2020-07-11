package com.xdkj.pc.service.mbigerServiceManage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xdkj.common.components.RateLimit;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.servicecallcost.bean.ServiceCallCost;
import com.xdkj.common.model.servicecallcost.dao.ServiceCallCostDao;
import com.xdkj.common.model.servicecalllimit.bean.ServiceCallLimit;
import com.xdkj.common.model.servicecalllimit.dao.ServiceCallLimitDao;
import com.xdkj.common.model.serviceinfo.bean.ServiceInfo;
import com.xdkj.common.model.serviceinfo.dao.ServiceInfoDao;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.user.dao.UserInfoDao;
import com.xdkj.common.model.userExpense.bean.UserExpense;
import com.xdkj.common.model.userExpense.dao.UserExpenseDao;
import com.xdkj.common.redis.service.CacheService;
import com.xdkj.common.redis.util.RedisUtil;
import com.xdkj.common.thirdparty.service.ThirdPartyCallService;
import com.xdkj.common.util.RandomUtil;
import com.xdkj.common.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description IT服务网提供服务接口实现
 * @Author zhangkele
 * @UpdateDate 2019/1/23 14:14
 */
@Service("mbigerService")
public class MbigerServiceImpl implements MbigerService {

    //接口频次时间窗口（1分钟10次）
    private static final int RATE_LIMIT_TIME = 1 * 60;

    //接口频次时间窗口（1分钟10次）
    private static final int RATE_LIMIT_COUNT = 10;

    private static final String STATUS = "status";

    private static final String MSG = "msg";

    @Autowired
    ServiceCallLimitDao serviceCallLimitDao;

    @Autowired
    ServiceCallCostDao serviceCallCostDao;

    @Autowired
    ServiceInfoDao serviceInfoDao;

    @Autowired
    UserExpenseDao userExpenseDao;

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    RateLimit rateLimit;

    @Autowired
    CacheService cacheService;
    @Autowired
    ThirdPartyCallService thirdPartyCallService;

    public int getRemainderFreeCount(String serviceCode, String menuType, String userId) {
        String freeCountKey = RedisUtil.keyBuilder("freeCount", serviceCode, userId);
        //已使用的免费次数
        Integer freeCount = (Integer) cacheService.getObject(freeCountKey);
        freeCount = freeCount == null ? 0 : freeCount;
        Integer defaultFreeCount = GlobalConstant.SERVICE_FREE_COUNT;
        ServiceCallLimit serviceCallLimit = serviceCallLimitDao.getServiceCallLimitByServiceCodeAndMenuType(serviceCode, menuType);
        if (serviceCallLimit != null) {
            defaultFreeCount = serviceCallLimit.getFreeLimit();
        }
        //剩余免费调用次数
        Integer remainderFreeCount = 0;
        if (freeCount <= defaultFreeCount) {
            remainderFreeCount = defaultFreeCount - freeCount;
        }
        return remainderFreeCount;
    }

    public BigDecimal getSingleCost(String serviceCode, String menuType) {
        ServiceCallCost serviceCallCost = null;
        serviceCallCost = serviceCallCostDao.getServiceCallCostByServiceCodeAndMenuType(serviceCode, menuType);
        if (serviceCallCost != null) {
            return serviceCallCost.getSingleCost();
        }
        return BigDecimal.ZERO;
    }

    @Transactional
    public String processBusiness(Map<String, String> params) {
        Map<String, String> resultMap = new HashMap<String, String>();
        //参数校验
        if (params == null || params.size() == 0) {
            resultMap.put(STATUS, "paramIsNull");
            resultMap.put(MSG, "参数不能为空");
            return JSON.toJSONString(resultMap);
        }
        String service = params.get("service");
        if (StringHelper.isEmpty(service)) {
            resultMap.put(STATUS, "paramIsNull");
            resultMap.put(MSG, "参数不能为空");
            return JSON.toJSONString(resultMap);
        }

        ServiceInfo serviceInfo = serviceInfoDao.getServiceInfoByServiceCode(service);
        if (serviceInfo == null) {
            resultMap.put(STATUS, "serviceInfoNotExist");
            resultMap.put(MSG, "调用的接口不存在");
            return JSON.toJSONString(resultMap);
        }

        //接口流量控制
        String rateLimitKey = params.get("rateLimitKey");
        if (StringHelper.isNotEmpty(rateLimitKey)) {
            params.remove("rateLimitKey");
            boolean allowFlag = rateLimit.allow(service, rateLimitKey, RATE_LIMIT_TIME, RATE_LIMIT_COUNT);
            if (!allowFlag) {
                resultMap.put(STATUS, "callFrequently");
                resultMap.put(MSG, "接口调用频繁，请稍后重试");
                return JSON.toJSONString(resultMap);
            }
        }


        //暂时只有默认配置
        String menuType = GlobalConstant.DEFAULT;
        /**
         * 接口调用次数控制
         *
         */
        ServiceCallLimit serviceCallLimit = serviceCallLimitDao.getServiceCallLimitByServiceCodeAndMenuType(service, menuType);
        if (serviceCallLimit == null) {
            resultMap.put(STATUS, "serviceCallLimitNotExist");
            resultMap.put(MSG, "接口次数配置信息不存在");
            return JSON.toJSONString(resultMap);
        }

        ServiceCallCost serviceCallCost = serviceCallCostDao.getServiceCallCostByServiceCodeAndMenuType(service, menuType);
        if (serviceCallCost == null) {
            resultMap.put(STATUS, "serviceCallCostNotExist");
            resultMap.put(MSG, "接口费用配置信息不存在");
            return JSON.toJSONString(resultMap);
        }
        //单次调用费用
        BigDecimal singleCost = serviceCallCost.getSingleCost();
        //接口调用是否免费
        boolean freeUseFlag = false;
        if (singleCost.compareTo(BigDecimal.ZERO) == 0) {
            freeUseFlag = true;
        }
        //用户id
        String userId = params.get("userId");
        //是否有用户信息
        boolean userExistFlag = false;
        UserInfo userInfo = null;
        if (StringHelper.isNotEmpty(userId)) {
            userInfo = userInfoDao.getUserInfoById(Integer.valueOf(userId));
            if (userInfo == null) {
                resultMap.put(STATUS, "userInfoNotExist");
                resultMap.put(MSG, "用户信息不存在");
                return JSON.toJSONString(resultMap);
            }
            userExistFlag = true;
        }
        //如果是收费接口，判断免费调用次数
        int defaultFreeCount = 0;
        String freeCountKey = null;
        Integer freeCount = 0;
        if (!freeUseFlag && userExistFlag) {
            //免费使用次数校验
            defaultFreeCount = serviceCallLimit.getFreeLimit();
            freeCountKey = RedisUtil.keyBuilder("freeCount", service, userId);
            freeCount = (Integer) cacheService.getObject(freeCountKey);
            freeCount = freeCount == null ? 0 : freeCount;
            if (freeCount < defaultFreeCount) {
                freeUseFlag = true;
            }
        }

        //接口调用费用校验
        if (!freeUseFlag && userExistFlag) {
            BigDecimal userAccountBalance = userInfo.getUserAccountBalance();
            if (userAccountBalance.compareTo(singleCost) < 0) {
                resultMap.put(STATUS, "AccountBalanceLess");
                resultMap.put(MSG, "账户余额不足");
                return JSON.toJSONString(resultMap);
            }
        }

        /**
         * 接口调用
         */
        //登录状态时，新增用户消费记录
        UserExpense userExpense = null;
        String ordId = null;
        if (userExistFlag) {
            ordId = RandomUtil.getSerialNumber();
            userExpense = new UserExpense();
            userExpense.setUserId(Integer.valueOf(userId));
            userExpense.setAppkey(userInfo.getAppkey());
            userExpense.setOrdId(ordId);
            userExpense.setUserName(userInfo.getUserName());
            userExpense.setServiceCode(serviceInfo.getServiceCode());
            userExpense.setServiceName(serviceInfo.getServiceName());
            userExpense.setExpenseAmount(singleCost);
            String expenseType = params.get("expenseType");
            expenseType = StringHelper.isEmpty(expenseType) ? "online" : expenseType;
            userExpense.setExpenseType(expenseType);
            userExpense.setStatus("1");
            userExpense.setDataStatus(GlobalConstant.DATA_VALID);
            userExpense.setCreateTime(new Date());
            userExpenseDao.addUserExpense(userExpense);
        }

        if (StringHelper.isEmpty(ordId)) {
            ordId = RandomUtil.getSerialNumber();
        }
        params.put("ordId", ordId);
        params.remove("expenseType");
        String respcontent = thirdPartyCallService.callThirdPartyAPI(params);
        JSONObject resultJson = JSON.parseObject(respcontent);
        //返回结果
        String status = resultJson.getString("status");
        //接口调用成功处理 ,（全国身份证实名认证，认证成功状态为：01）
        if ("0".equals(status) || ("01".equals(status) && "identityCardIdentify".equals(service))) {
            if (userExpense != null) {
                //修改用户消费记录状态
                userExpenseDao.updateUserExpenseStatusByAppKey(userInfo.getAppkey(), "0");
                if (!freeUseFlag) {
                    //免费使用次数状态为false，修改用户账户金额
                    userInfoDao.updateUserAccountBalance(userInfo.getId(), userInfo.getUserAccountBalance().subtract(singleCost));
                }
            }
        }
        //增加免费调用次数
        if (freeUseFlag && (defaultFreeCount > 0)) {
            freeCount++;
            cacheService.set(freeCountKey, freeCount);
        }
        return respcontent;
    }

    public ServiceInfo getServiceInfoByCode(String serviceCode) {
        return serviceInfoDao.getServiceInfoByServiceCode(serviceCode);
    }

    public List<ServiceInfo> listServiceInfosByServiceModule(String serviceModule) {
        return serviceInfoDao.listServiceInfosByServiceModule(serviceModule);
    }
}
