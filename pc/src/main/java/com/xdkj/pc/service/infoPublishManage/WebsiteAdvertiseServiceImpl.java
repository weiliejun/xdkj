package com.xdkj.pc.service.infoPublishManage;

import com.xdkj.common.model.websiteAdvertise.bean.WebsiteAdvertise;
import com.xdkj.common.model.websiteAdvertise.dao.WebsiteAdvertiseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("websiteAdvertiseService")
public class WebsiteAdvertiseServiceImpl implements WebsiteAdvertiseService {

    @Autowired
    private WebsiteAdvertiseDao advertiseDao;

    public WebsiteAdvertise addAdvertise(WebsiteAdvertise record) {
        return advertiseDao.addAdvertise(record);
    }

    public int updateAdvertise(WebsiteAdvertise record) {
        return advertiseDao.updateAdvertise(record);
    }

    public WebsiteAdvertise getAdvertiseById(Integer id) {
        return advertiseDao.getAdvertiseById(id);
    }

    public void updateAdvertiseClicks(String code) {
        advertiseDao.updateAdvertiseClicks(code);
    }

    public List<WebsiteAdvertise> listAdvertisesByParams(Map<String, String> params) {
        return advertiseDao.listAdvertisesByParams(params);
    }
}
