package com.xdkj.common.model.websiteBulletin.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.websiteBulletin.bean.WebsiteBulletin;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class WebsiteBulletinDao extends AbstractBaseDao {

    public void addWebsiteBulletin(WebsiteBulletin websiteBulletin) {
        insert("websiteBulletin.addWebsiteBulletin", websiteBulletin);
    }

    public WebsiteBulletin getWebsiteBulletinById(Integer id) {
        return (WebsiteBulletin) queryForObject("websiteBulletin.getWebsiteBulletinById", id);
    }

    public void updateWebsiteBulletin(WebsiteBulletin websiteBulletin) {
        queryForObject("websiteBulletin.updateWebsiteBulletin", websiteBulletin);
    }

    public int countWebsiteBulletinsByParams(Map<String, Object> params) {
        return (int) queryForObject("websiteBulletin.countWebsiteBulletinsByParams", params);
    }

    public List<Map<String, Object>> listWebsiteBulletinsByParams(Map<String, Object> params) {
        return queryForList("websiteBulletin.listWebsiteBulletinsByParams", params);
    }

    public List<WebsiteBulletin> listWebsiteBulletinByParams(Map<String, Object> params) {
        return queryForList("websiteBulletin.listWebsiteBulletinByParams", params);
    }
}