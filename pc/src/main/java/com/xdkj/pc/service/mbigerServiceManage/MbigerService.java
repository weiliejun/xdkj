package com.xdkj.pc.service.mbigerServiceManage;


import com.xdkj.common.model.serviceinfo.bean.ServiceInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description IT服务网提供服务接口
 * @auther: zhangkele
 * @UpadteDate: 2019/1/23 13:54
 */
public interface MbigerService {

    /**
     * @Description 获取接口免费调用剩余次数
     * @auther: zhangkele
     * @UpadteDate: 2019/1/23 13:56
     */
    int getRemainderFreeCount(String serviceCode, String menuType, String userId);

    /**
     * @Description 获取接口单次调用的费用
     * @auther: zhangkele
     * @UpadteDate: 2019/1/23 13:56
     */
    BigDecimal getSingleCost(String serviceCode, String menuType);

    /**
     * @Description 调用服务接口
     * @auther: zhangkele
     * @UpadteDate: 2019/1/23 14:12
     */
    String processBusiness(Map<String, String> params);

    /**
     * @Description 根据serviceCode查询服务信息
     * @auther: FENG.yanmin
     * @UpadteDate: 2019/1/24 14:12
     */
    ServiceInfo getServiceInfoByCode(String serviceCode);

    /**
     * @Description 根据serviceModule查询服务信息
     * @auther: FENG.yanmin
     * @UpadteDate: 2019/1/24 14:12
     */
    List<ServiceInfo> listServiceInfosByServiceModule(String serviceModule);
}
