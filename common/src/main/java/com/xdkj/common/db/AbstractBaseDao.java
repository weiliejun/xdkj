package com.xdkj.common.db;

import com.xdkj.common.redis.dao.CacheDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

public abstract class AbstractBaseDao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    protected CacheDao cacheDao;


    public <T> T queryForObject(String statementName)
            throws DataAccessException {
        return sqlSessionTemplate.selectOne(statementName);
    }

    public Object queryForObject(final String statementName,
                                 final Object parameter) throws DataAccessException {
        return sqlSessionTemplate.selectOne(statementName, parameter);
    }

    public Object queryForObject(final String statementName,
                                 final String parameter) throws DataAccessException {
        return sqlSessionTemplate.selectOne(statementName, parameter);
    }

    public List queryForList(String statementName) throws DataAccessException {
        return sqlSessionTemplate.selectList(statementName);
    }

    public List queryForList(final String statementName, final Object parameter)
            throws DataAccessException {
        return sqlSessionTemplate.selectList(statementName, parameter);
    }

    public Object insert(String statementName) throws DataAccessException {
        return sqlSessionTemplate.insert(statementName);
    }

    public Object insert(final String statementName,
                         final Object parameterObject) throws DataAccessException {
        return sqlSessionTemplate.insert(statementName, parameterObject);
    }

    public int update(String statementName) throws DataAccessException {
        return sqlSessionTemplate.update(statementName);
    }

    public int update(final String statementName, final Object parameterObject)
            throws DataAccessException {
        return sqlSessionTemplate.update(statementName, parameterObject);
    }

    public int delete(String statementName) throws DataAccessException {
        return sqlSessionTemplate.delete(statementName);
    }

    public int delete(final String statementName, final Object parameterObject)
            throws DataAccessException {
        return sqlSessionTemplate.delete(statementName, parameterObject);
    }


}
