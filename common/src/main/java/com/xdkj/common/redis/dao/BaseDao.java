package com.xdkj.common.redis.dao;

import com.xdkj.common.redis.StringRedisTemplate;
import org.springframework.data.redis.core.RedisCommand;


public interface BaseDao {
    StringRedisTemplate getStringRedisTemplate(RedisCommand command);
}
