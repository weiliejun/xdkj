package com.xdkj.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCommand;
import org.springframework.stereotype.Repository;


@Repository("RedisProxy")
public class RedisProxyImpl implements RedisProxy {
    /* READ ONLY for SLAVES */
    @Autowired
    private ReadStringRedisTemplate rsrt;
    @Autowired
    private ReadRedisTemplate rrt;

    /* WRITE for MASTER */
    @Autowired
    private WriteStringRedisTemplate wsrt;
    @Autowired
    private WriteRedisTemplate wrt;

    @Override
    public StringRedisTemplate getStringRedisTemplate(RedisCommand command) {
        if (command.isReadonly()) {
            return rsrt;
        } else if (command.isWrite()) {
            return wsrt;
        } else {
            return wsrt;
        }
    }

    @Override
    public RedisTemplate getRedisTemplate(RedisCommand command) {
        if (command.isReadonly()) {
            return rrt;
        } else if (command.isWrite()) {
            return wrt;
        } else {
            return wrt;
        }

    }

    @Override
    public RedisTemplate getMasterRedisTemplate() {
        return wrt;
    }

    @Override
    public StringRedisTemplate getMasterStringRedisTemplate() {
        return wsrt;
    }
}
