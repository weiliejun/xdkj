package com.xdkj.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Repository;

/**
 * redis 数据库操作模板
 *
 * @author
 * @version 1.0
 */
@Repository
public class StringRedisTemplate extends org.springframework.data.redis.core.StringRedisTemplate {

    @Override
    @Autowired(required = false)
    @Qualifier("jedisConnectionFactory")
    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        super.setConnectionFactory(connectionFactory);
    }
}
