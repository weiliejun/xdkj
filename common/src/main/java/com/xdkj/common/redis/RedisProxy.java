package com.xdkj.common.redis;

import org.springframework.data.redis.core.RedisCommand;

public interface RedisProxy {

    public RedisTemplate getRedisTemplate(RedisCommand command);

    public StringRedisTemplate getStringRedisTemplate(RedisCommand command);

    public RedisTemplate getMasterRedisTemplate();

    public StringRedisTemplate getMasterStringRedisTemplate();
}
