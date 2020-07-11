package com.xdkj.admin.system.service;

import com.xdkj.common.model.sysdictionary.bean.SysDictionary;
import com.xdkj.common.model.sysdictionary.dao.SysDictionaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("sysDictionaryService")
public class SysDictionaryServiceImpl implements SysDictionaryService {

    @Autowired
    SysDictionaryDao sysDictionaryDao;


    public void addSysDictionary(SysDictionary sysDictionary) {
        sysDictionaryDao.addSysDictionary(sysDictionary);
    }

    public void updateSysDictionary(SysDictionary sysDictionary) {
        sysDictionaryDao.updateSysDictionary(sysDictionary);
    }

    public SysDictionary getSysDictionaryById(Integer id) {
        return sysDictionaryDao.getSysDictionaryById(id);
    }

    public SysDictionary getSysDictionaryByCode(String parentCode, String code) {
        return sysDictionaryDao.getSysDictionaryByCode(parentCode, code);
    }

    public SysDictionary getSysDictionaryByCode(String code) {
        return sysDictionaryDao.getSysDictionaryByCode(code);
    }

    public SysDictionary getSysDictionaryByName(String parentCode, String name, String isHasChild) {
        return sysDictionaryDao.getSysDictionaryByName(parentCode, name, isHasChild);
    }

    public List<SysDictionary> listSysDictionaryByParams(Map<String, Object> params) {
        return sysDictionaryDao.listSysDictionarysByParams(params);
    }
}
