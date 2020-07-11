package com.xdkj.common.model.sysdictionary.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.sysdictionary.bean.SysDictionary;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@CacheConfig(cacheNames = "dictionary")
public class SysDictionaryDao extends AbstractBaseDao {

    public void addSysDictionary(SysDictionary sysDictionary) {
        insert("sysdictionary.addSysDictionary", sysDictionary);
    }

    @CacheEvict(allEntries = true)
    public void updateSysDictionary(SysDictionary sysDictionary) {
        update("sysdictionary.updateSysDictionary", sysDictionary);
    }


    public SysDictionary getSysDictionaryById(Integer id) {
        return (SysDictionary) queryForObject("sysdictionary.getSysDictionaryById", id);
    }


    public List<SysDictionary> listSysDictionarysByParams(Map<String, Object> params) {
        return queryForList("sysdictionary.listSysDictionarysByParams", params);
    }

    @Cacheable(key = "#parentCode", unless = "#result == null")
    public List<SysDictionary> getSysDictionarysByParentCode(String parentCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentCode", parentCode);
        List<SysDictionary> sysDictionaryList = listSysDictionarysByParams(params);
        return sysDictionaryList;
    }

    @Cacheable(key = "#parentCode + ':' + #code", unless = "#result == null")
    public SysDictionary getSysDictionaryByCode(String parentCode, String code) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentCode", parentCode);
        params.put("code", code);
        List<SysDictionary> sysDictionaryList = listSysDictionarysByParams(params);
        if (sysDictionaryList != null && sysDictionaryList.size() > 0) {
            return sysDictionaryList.get(0);
        }
        return null;
    }

    public SysDictionary getSysDictionaryByCode(String code) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        List<SysDictionary> sysDictionaryList = listSysDictionarysByParams(params);
        if (sysDictionaryList != null && sysDictionaryList.size() > 0) {
            return sysDictionaryList.get(0);
        }
        return null;
    }

    public SysDictionary getSysDictionaryByName(String parentCode, String name, String isHasChild) {
        Map<String, Object> params = new HashMap<String, Object>();
        if ("0".equals(isHasChild)) {
            params.put("name", name);
        } else {
            params.put("parentCode", parentCode);
            params.put("name", name);
        }

        List<SysDictionary> sysDictionaryList = listSysDictionarysByParams(params);
        if (sysDictionaryList != null && sysDictionaryList.size() > 0) {
            return sysDictionaryList.get(0);
        }
        return null;
    }
}
