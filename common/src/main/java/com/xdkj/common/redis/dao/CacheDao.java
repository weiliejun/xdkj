package com.xdkj.common.redis.dao;

import com.xdkj.common.redis.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface CacheDao {

    /**
     * 赋值
     *
     * @param key
     * @param value
     */
    void set(String key, Object value);

    /**
     * @param key
     * @param value
     * @param timeout 缓存时间，单位秒
     */
    void set(String key, Object value, long timeout);

    /**
     * 赋值
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * 赋值
     *
     * @param key     键
     * @param value   值
     * @param timeout 缓存时间，单位秒
     */
    void set(String key, String value, long timeout);

    /**
     * 根据通配符获取keys
     *
     * @param keyPattern key通配符
     */
    Set<String> getKeys(String keyPattern);

    /**
     * key 是否存在
     *
     * @param key
     */
    boolean hasKey(String key);

    /**
     * 获取KEY值
     *
     * @param key
     * @return
     */
    <T> T getObject(String key);

    /**
     * 获取KEY值
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 移除KEY(物理删除)
     *
     * @param key
     */
    void remove(String key);


    /**
     * 获取Set大小
     *
     * @param key
     * @return
     */
    long getSetSize(String key);

    /**
     * 移除SET一个值
     *
     * @param key
     * @param values
     */
    void removeSet(String key, List<Object> values);

    /**
     * 获取SET所有值
     *
     * @param key
     * @return
     */
    Set<String> getSet(String key);

    /**
     * 向SET添加一个值
     *
     * @param key
     * @param values
     */
    void addSet(String key, List<String> values);

    /**
     * 设置KEY缓存时间
     *
     * @param key
     * @param timeout 缓存时间，单位秒
     */
    void expire(String key, long timeout);

    /**
     * 获取HASH字段值
     *
     * @param key
     * @param field
     * @return
     */
    <T> T getObject(String key, String field);

    /**
     * HASH赋值
     *
     * @param key
     * @param field
     * @param value
     */
    void mapPush(String key, String field, Object value);

    /**
     * 移除HASH字段值
     *
     * @param key
     * @param field
     */
    void remove(String key, String field);

    List<String> mget(Collection<String> keys);

    public void mset(Map<String, String> map);

    StringRedisTemplate getMasterStringRedisTemplate();

    /**
     * 获取key的过期时间
     *
     * @param key
     * @return
     */
    boolean ttl(String key);

    void listRPush(String key, String value);


    void listLPush(String key, String value);

    String listRpop(String key);

    long listLen(String key);

    /**
     * 获取有序集合中排名
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    Set<TypedTuple<String>> zrange(String key, int start, int end, boolean reverse);


    public List<String> getRange(String key, long begin, long end);

    public List<String> getRrange(String key, long begin, long end);

    public Map<Object, Object> hashGetAll(String key);

    public List<Object> mgetObject(List<String> keys);

    public Map<String, Map<String, Integer>> hashMGetall(final List<String> keys);

    Set<TypedTuple<String>> zrangebyscore(String key, double min, double max, boolean reverse);

    Long decrement(String key);

    Map<Object, Object> hashGetAllNew(String key);

    public void mapPushAll(String key, Map<String, Object> map);
}
