package com.xdkj.common.components;

import com.xdkj.common.redis.util.RedisUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;


@Component
public class RateLimit {
    private static final Logger logger = Logger.getLogger(RateLimit.class);
    private static final String RATE_LIMIT = "rateLimit";


    @Resource
    JedisPool idJedisPools;

    /**
     * 流量控制,允许访问返回true 不允许访问返回false
     *
     * @param key     放入redis的key
     * @param timeOut 超时时间单位秒
     * @param count   超时时间内允许访问的次数
     * @param type    不同类型的数据
     */
    public boolean allow(String type, String key, int timeOut, int count) {
        // 若不使用流量控制直接返回true
        if (false) {
            return true;
        }
        boolean result = false;
        Jedis jedis = idJedisPools.getResource();
        key = RedisUtil.keyBuilder(RATE_LIMIT, type, key);
        try {
            jedis.connect();
            Long newTimes = null;
            Long pttl = jedis.pttl(key);
            if (pttl > 0) {
                newTimes = jedis.incr(key);
                if (newTimes > count) {
                    logger.info("key:{" + key + "},超出{" + timeOut + "}秒内允许访问{" + count + "}次的限制,这是第{" + newTimes + "}次访问");
                } else {
                    result = true;
                }
            } else if (pttl == -1 || pttl == -2 || pttl == 0) {

                Transaction tx = jedis.multi();
                Response<Long> rsp1 = tx.incr(key);
                tx.expire(key, timeOut);
                tx.exec();
                newTimes = rsp1.get();
                if (newTimes > count) {
                    logger.info("key:{" + key + "},{" + timeOut + "}秒内允许访问{}次,第{" + newTimes + "}次访问");
                } else {
                    result = true;
                }
            }
            if (result) {
                logger.debug("key:{" + key + "},访问次数{" + newTimes + "}");
            }
        } catch (Exception e) {
            logger.error("流量控制发生异常", e);
            // 当发生异常时 允许访问  
            result = true;
        } finally {
            jedis.close();
        }
        return result;

    }
}