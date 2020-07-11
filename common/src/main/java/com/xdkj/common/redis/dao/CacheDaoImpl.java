package com.xdkj.common.redis.dao;

import com.xdkj.common.redis.StringRedisTemplate;
import com.xdkj.common.util.CommonUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisCommand;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository("CacheDao")
public class CacheDaoImpl extends BaseDaoImpl implements CacheDao {

    @Override
    public void set(String key, Object value) {
        getRedisTemplate(RedisCommand.SET).opsForValue().set(key, value);
    }

    @Override
    public void set(String key, Object value, long timeout) {
        getRedisTemplate(RedisCommand.SET).opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, String value) {
        getStringRedisTemplate(RedisCommand.SET).opsForValue().set(key, value);
    }

    @Override
    public void mset(Map<String, String> map) {
        getStringRedisTemplate(RedisCommand.MSET).opsForValue().multiSet(map);
    }

    @Override
    public void set(String key, String value, long timeout) {
        getStringRedisTemplate(RedisCommand.SET).opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public Set<String> getKeys(String keyPattern) {
        Set<String> keys = getStringRedisTemplate(RedisCommand.KEYS).keys(keyPattern);
        if (keys != null && !keys.isEmpty()) {
            return keys;
        }
        return null;
    }

    /**
     * 键是否存在
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return getStringRedisTemplate(RedisCommand.EXISTS).hasKey(key);
    }

    @Override
    public void expire(String key, long timeout) {
        if (getStringRedisTemplate(RedisCommand.EXISTS).hasKey(key)) {
            getStringRedisTemplate(RedisCommand.EXPIRE).expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    public <T> T getObject(String key) {
        if (ttl(key) && getRedisTemplate(RedisCommand.EXISTS).hasKey(key)) {
            return (T) getRedisTemplate(RedisCommand.GET).opsForValue().get(key);
        }
        return null;
    }

    @Override
    public List<String> mget(Collection<String> keys) {
        return getStringRedisTemplate(RedisCommand.MGET).opsForValue().multiGet(keys);
    }

    @Override
    public List<Object> mgetObject(List<String> keys) {
        return getRedisTemplate(RedisCommand.MGET).opsForValue().multiGet(keys);
    }

    @Override
    public String get(String key) {
        if (ttl(key) && getStringRedisTemplate(RedisCommand.EXISTS).hasKey(key)) {
            return getStringRedisTemplate(RedisCommand.GET).opsForValue().get(key);
        }
        return null;
    }

    @Override
    public void addSet(String key, List<String> values) {
        if (!CommonUtil.isEmpty(values)) {
            String[] vs = values.toArray(new String[0]);
            getStringRedisTemplate(RedisCommand.SADD).opsForSet().add(key, vs);
        }
    }

    @Override
    public Set<String> getSet(String key) {
        if (ttl(key) && getStringRedisTemplate(RedisCommand.EXISTS).hasKey(key)) {
            return getStringRedisTemplate(RedisCommand.SMEMBERS).opsForSet().members(key);
        }
        return null;
    }

    @Override
    public void removeSet(String key, List<Object> values) {
        if (hasKey(key)) {
            if (!CommonUtil.isEmpty(values)) {
                Object[] vs = values.toArray(new Object[0]);
                getStringRedisTemplate(RedisCommand.SREM).opsForSet().remove(key, vs);
            }
        }
    }

    @Override
    public long getSetSize(String key) {
        return getStringRedisTemplate(RedisCommand.SCARD).opsForSet().size(key);
    }

    @Override
    public void mapPush(String key, String field, Object value) {
        getRedisTemplate(RedisCommand.HSET).opsForHash().put(key, field, value);
    }

    @Override
    public void mapPushAll(String key, Map<String, Object> map) {
        getRedisTemplate(RedisCommand.HSET).opsForHash().putAll(key, map);
    }

    @Override
    public <T> T getObject(String key, String field) {
        if (ttl(key) && hasKey(key)) {
            if (getRedisTemplate(RedisCommand.HEXISTS).opsForHash().hasKey(key, field)) {
                return (T) getRedisTemplate(RedisCommand.HGET).opsForHash().get(key, field);
            }
            return null;
        }
        return null;
    }

    @Override
    public void remove(String key, String field) {
        if (hasKey(key)) {
            if (getRedisTemplate(RedisCommand.HEXISTS).opsForHash().hasKey(key, field)) {
                getRedisTemplate(RedisCommand.HDEL).opsForHash().delete(key, field);
            }
        }
    }

    @Override
    public void remove(String key) {
        if (hasKey(key)) {
            getStringRedisTemplate(RedisCommand.DEL).delete(key);
        }
    }


    @Override
    public StringRedisTemplate getMasterStringRedisTemplate() {
        return getStringRedisTemplate(RedisCommand.EXPIRE);
    }

    @Override
    public boolean ttl(String key) {
        long expire = getStringRedisTemplate(RedisCommand.TTL).getExpire(key);
        return (expire > 0 || expire == -1);
    }

    @Override
    public List<String> getRange(String key, long begin, long end) {
        return getStringRedisTemplate(RedisCommand.LRANGE).opsForList().range(key, begin, end);
    }

    @Override
    public List<String> getRrange(String key, long begin, long end) {
        return getStringRedisTemplate(RedisCommand.RANAME).opsForList().range(key, begin, end);
    }

    @Override
    public void listRPush(String key, String value) {
        getStringRedisTemplate(RedisCommand.RPUSH).opsForList().rightPush(key, value);
    }

    @Override
    public void listLPush(String key, String value) {
        getStringRedisTemplate(RedisCommand.LPUSH).opsForList().leftPush(key, value);
    }


    @Override
    public String listRpop(String key) {
        return getStringRedisTemplate(RedisCommand.RPOP).opsForList().rightPop(key);
    }

    @Override
    public long listLen(String key) {
        return getStringRedisTemplate(RedisCommand.LLEN).opsForList().size(key);
    }

    @Override
    public Set<TypedTuple<String>> zrange(String key, int start, int end, boolean reverse) {
        if (reverse == true) {
            return getStringRedisTemplate(RedisCommand.ZREVRANGE).opsForZSet().reverseRangeWithScores(key, start, end);
        } else {
            return getStringRedisTemplate(RedisCommand.ZRANGE).opsForZSet().rangeWithScores(key, start, end);
        }
    }

    @Override
    public Set<TypedTuple<String>> zrangebyscore(String key, double min, double max, boolean reverse) {
        if (reverse == true) {
            return getStringRedisTemplate(RedisCommand.ZREVRANGEBYSCORE).opsForZSet().reverseRangeByScoreWithScores(key, min, max);
        } else {
            return getStringRedisTemplate(RedisCommand.ZRANGEBYSCORE).opsForZSet().rangeByScoreWithScores(key, min, max);
        }
    }

    @Override
    public Map<Object, Object> hashGetAll(String key) {
        return getStringRedisTemplate(RedisCommand.HGETALL).opsForHash().entries(key);
    }

    @Override
    public Map<String, Map<String, Integer>> hashMGetall(final List<String> keys) {
        if (CommonUtil.isEmpty(keys)) {
            return null;
        }
        StringRedisTemplate rt = getMasterStringRedisTemplate();
        final RedisSerializer<String> rs = (RedisSerializer<String>) rt.getStringSerializer();
        final RedisSerializer<String> hks = (RedisSerializer<String>) rt.getHashKeySerializer();
        final RedisSerializer<String> hvs = (RedisSerializer<String>) rt.getHashValueSerializer();
        RedisCallback<List<Object>> pipelineCallback = new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for (String key : keys) {
                    connection.hGetAll(rs.serialize(key));
                }
                return connection.closePipeline();
            }
        };
        List<Object> results = rt.execute(pipelineCallback);
        Map<String, Map<String, Integer>> rst = new HashMap<String, Map<String, Integer>>();
        for (int i = 0; i < keys.size(); i++) {
            if (!CommonUtil.isEmpty(results.get(i))) {
                Map<byte[], byte[]> map = (Map<byte[], byte[]>) results.get(i);
                Map<String, Integer> decodemap = new HashMap<String, Integer>();
                for (byte[] k : map.keySet()) {
                    decodemap.put(hks.deserialize(k), Integer.valueOf(hvs.deserialize(map.get(k))));
                }
                if (map.size() > 0) {
                    rst.put(keys.get(i), decodemap);
                }
            }
        }
        return rst;
    }

    @Override
    public Long decrement(String key) {
        return getStringRedisTemplate(RedisCommand.DECRBY).opsForValue().increment(key, -1L);
    }

    @Override
    public Map<Object, Object> hashGetAllNew(String key) {
        return getRedisTemplate(RedisCommand.HGETALL).opsForHash().entries(key);
    }
}
