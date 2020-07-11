package com.xdkj.admin.system.service;


import com.xdkj.common.model.sysdictionary.bean.SysDictionary;

import java.util.List;
import java.util.Map;

public interface SysDictionaryService {

    void addSysDictionary(SysDictionary sysDictionary);

    void updateSysDictionary(SysDictionary sysDictionary);

    SysDictionary getSysDictionaryById(Integer id);

    SysDictionary getSysDictionaryByCode(String parentCode, String code);

    /* code 校验唯一性 */
    SysDictionary getSysDictionaryByCode(String code);

    /**
     * @param parentCode 父级编码
     * @param name       节点名称
     * @param isHasChild 是否有子节点（1-无，0-有）
     * @return
     * @discription 校验名称唯一性
     * 说明：若是子级名称，同一个父级类型下 类型名称不能相同；若是父级名称，名称唯一性
     */
    /* */
    SysDictionary getSysDictionaryByName(String parentCode, String name, String isHasChild);

    List<SysDictionary> listSysDictionaryByParams(Map<String, Object> params);
}
