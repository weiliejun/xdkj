package com.xdkj.common.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class RedisCluster implements FactoryBean<JedisCluster>, InitializingBean {

    private JedisCluster jedisCluster;
    private String host;
    private String port;
    private GenericObjectPoolConfig genericObjectPoolConfig;
    private String timeout;

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> haps = new HashSet<HostAndPort>();
        haps.add(new HostAndPort(this.host, Integer.valueOf(this.port)));
        jedisCluster = new JedisCluster(haps, Integer.valueOf(timeout), genericObjectPoolConfig);

    }

    @Override
    public JedisCluster getObject() throws Exception {
        // TODO Auto-generated method stub
        return jedisCluster;
    }

    @Override
    public Class<?> getObjectType() {
        return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);
    }

    @Override
    public boolean isSingleton() {
        // TODO Auto-generated method stub
        return true;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        return genericObjectPoolConfig;
    }

    public void setGenericObjectPoolConfig(
            GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

}
