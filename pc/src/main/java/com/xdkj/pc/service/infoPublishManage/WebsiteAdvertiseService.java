package com.xdkj.pc.service.infoPublishManage;

import com.xdkj.common.model.websiteAdvertise.bean.WebsiteAdvertise;

import java.util.List;
import java.util.Map;

public interface WebsiteAdvertiseService {

    WebsiteAdvertise addAdvertise(WebsiteAdvertise record);

    int updateAdvertise(WebsiteAdvertise record);

    WebsiteAdvertise getAdvertiseById(Integer id);

    void updateAdvertiseClicks(String code);

    List<WebsiteAdvertise> listAdvertisesByParams(Map<String, String> params);


}