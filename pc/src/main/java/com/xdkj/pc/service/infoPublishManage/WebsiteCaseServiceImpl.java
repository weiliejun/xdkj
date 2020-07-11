package com.xdkj.pc.service.infoPublishManage;

import com.xdkj.common.model.websiteCase.bean.WebsiteCase;
import com.xdkj.common.model.websiteCase.dao.WebsiteCaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("websiteCaseService")
public class WebsiteCaseServiceImpl implements WebsiteCaseService {

    @Autowired
    private WebsiteCaseDao websiteCaseDao;

    public WebsiteCase addCase(WebsiteCase record) {
        return websiteCaseDao.addCase(record);
    }

    public int updateCase(WebsiteCase record) {
        return websiteCaseDao.updateCase(record);
    }

    public WebsiteCase getCaseById(Integer id) {
        return websiteCaseDao.getCaseById(id);
    }

    public List<WebsiteCase> listCasesByParams(Map<String, String> params) {
        return websiteCaseDao.listCasesByParams(params);
    }
}
