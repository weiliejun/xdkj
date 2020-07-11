package com.xdkj.common.thirdparty.service;

import com.xdkj.common.thirdparty.util.MbigerServiceUtil;
import com.xdkj.common.util.APIUrlHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Description 第三方接口调用实现
 * @Author zhangkele
 * @UpdateDate 2019/1/23 12:40
 */
@Service("thirdPartyCallService")
public class ThirdPartyCallServiceImpl implements ThirdPartyCallService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public String callThirdPartyAPI(Map<String, String> params) {
        String serivce = params.get("service");
        APIUrlHelper.APIName apiName = APIUrlHelper.APIName.getAPIName(serivce);
        //移除参数
        params.remove("userId");
        params.remove("appKey");
        params.remove("ordId");
        params.remove("service");
        params.remove("rateLimitKey");
        params.remove("expenseType");
        return MbigerServiceUtil.httpPost(APIUrlHelper.getRequestUrl(apiName), params);
    }
}
