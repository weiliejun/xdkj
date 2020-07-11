package com.xdkj.common.model.websiteCase.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.websiteCase.bean.WebsiteCase;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class WebsiteCaseDao extends AbstractBaseDao {

    public WebsiteCase addCase(WebsiteCase record) {
        record = (WebsiteCase) insert("websiteCase.addCase", record);
        return record;
    }

    public int updateCase(WebsiteCase record) {
        return update("websiteCase.updateCase", record);
    }

    public WebsiteCase getCaseById(Integer id) {
        return (WebsiteCase) queryForObject("websiteCase.getCaseById", id);
    }

    public List<WebsiteCase> listCasesByParams(Map<String, String> params) {
        List<WebsiteCase> results = queryForList("websiteCase.listCasesByParams", params);
        return results;
    }

}