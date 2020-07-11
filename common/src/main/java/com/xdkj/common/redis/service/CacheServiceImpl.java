package com.xdkj.common.redis.service;

import com.xdkj.common.redis.dao.CacheDao;
import com.xdkj.common.util.CommonUtil;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis 缓存管理
 */
@Repository("CacheService")
public class CacheServiceImpl implements CacheService {

    @Resource(name = "CacheDao")
    private CacheDao cacheDao;

    @Override
    public void set(String key, Object value) {
        cacheDao.set(key, value);
    }

    @Override
    public void set(String key, Object value, long timeout) {
        cacheDao.set(key, value, timeout);
    }

    @Override
    public void set(String key, String value) {
        cacheDao.set(key, value);
    }

    @Override
    public void set(String key, String value, long timeout) {
        cacheDao.set(key, value, timeout);
    }

    @Override
    public Set<String> getKeys(String keyPattern) {
        return cacheDao.getKeys(keyPattern);
    }

    @Override
    public boolean hasKey(String key) {
        return cacheDao.hasKey(key);
    }

    @Override
    public <T> T getObject(String key) {
        return cacheDao.getObject(key);
    }

    @Override
    public String get(String key) {
        return cacheDao.get(key);
    }

    @Override
    public String get(String key, int retry) {
        String res = null;
        while (retry > 0) {
            res = cacheDao.get(key);
            if (CommonUtil.isNotEmpty(res)) {
                return res;
            } else {
                retry--;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                ;
            }
        }
        return res;
    }

    @Override
    public void remove(String key) {
        cacheDao.remove(key);
    }


    @Override
    public long getSetSize(String key) {
        return cacheDao.getSetSize(key);
    }

    @Override
    public void removeSet(String key, List<Object> values) {
        cacheDao.removeSet(key, values);
    }

    @Override
    public Set<String> getSet(String key) {
        return cacheDao.getSet(key);
    }

    @Override
    public void addSet(String key, List<String> value) {
        cacheDao.addSet(key, value);
    }

    @Override
    public void expire(String key, long timeout) {
        cacheDao.expire(key, timeout);
    }

    @Override
    public <T> T getObject(String key, String field) {
        return cacheDao.getObject(key, field);
    }

    @Override
    public void mapPush(String key, String field, Object value) {
        cacheDao.mapPush(key, field, value);
    }

    @Override
    public void remove(String key, String field) {
        cacheDao.remove(key, field);
    }

    @Override
    public void publish(String channel, String message) {

    }

    @Override
    public void publish(String channel, Object message) {
        cacheDao.getMasterStringRedisTemplate().convertAndSend(channel, message);
    }

    @Override
    public List<String> mget(Collection<String> keys) {
        return cacheDao.mget(keys);
    }

    @Override
    public void mset(Map<String, String> map) {
        cacheDao.mset(map);
    }

    @Override
    public void listrpush(String key, String value) {
        cacheDao.listRPush(key, value);

    }

    @Override
    public Set<TypedTuple<String>> zrange(String key, int start, int end, boolean reverse) {
        return cacheDao.zrange(key, start, end, reverse);
    }

    @Override
    public List<String> getRange(String key, long begin, long end) {
        return cacheDao.getRange(key, begin, end);
    }

    @Override
    public List<String> getRrange(String key, long begin, long end) {
        return cacheDao.getRrange(key, begin, end);
    }

    @Override
    public Map<Object, Object> hashGetAll(String key) {
        return cacheDao.hashGetAll(key);
    }

    @Override
    public List<Object> mgetObject(List<String> keys) {
        return cacheDao.mgetObject(keys);
    }

    @Override
    public Map<String, Map<String, Integer>> hashMGetall(final List<String> keys) {
        return cacheDao.hashMGetall(keys);
    }

    @Override
    public Set<TypedTuple<String>> zrangebyscore(String key, double min, double max, boolean reverse) {
        return cacheDao.zrangebyscore(key, min, max, reverse);
    }

    @Override
    public void listLpush(String key, String value) {
        cacheDao.listLPush(key, value);
    }

    @Override
    public String listRpop(String key) {
        return cacheDao.listRpop(key);
    }

    @Override
    public long listLen(String key) {
        return cacheDao.listLen(key);
    }

    @Override
    public Long decrement(String key) {
        return cacheDao.decrement(key);
    }

    @Override
    public Map<Object, Object> hashGetAllNew(String key) {
        return cacheDao.hashGetAllNew(key);
    }

    @Override
    public void mapPushAll(String key, Map<String, Object> map) {
        cacheDao.mapPushAll(key, map);
    }
}
