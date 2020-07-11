package com.xdkj.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ReadStringRedisTemplate extends StringRedisTemplate {
    @Override
    @Autowired(required = false)
    @Qualifier(value = "jedisConnectionFactory")
    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        super.setConnectionFactory(connectionFactory);
    }
}
