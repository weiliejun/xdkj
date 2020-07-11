package com.xdkj.common.model.sysBusiness.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.sysBusiness.bean.SysBusiness;
import org.springframework.stereotype.Repository;

@Repository
public class SysBusinessDao extends AbstractBaseDao {
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    public int insert(SysBusiness record) {
        return 0;
    }

    public int insertSelective(SysBusiness record) {
        return 0;
    }

    public SysBusiness selectByPrimaryKey(Integer id) {
        return null;
    }

    public int updateByPrimaryKeySelective(SysBusiness record) {
        return 0;
    }

    public int updateByPrimaryKey(SysBusiness record) {
        return 0;
    }
}