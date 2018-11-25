package com.skt.ehs.mbs.db.redis;

/**
 * Created by user on 2016-11-17.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisConnectionPool {
    private static Logger logger = LoggerFactory.getLogger(JedisConnectionPool.class);
    static JedisPool jedisPool;

    public JedisConnectionPool() {
        jedisPool = null;
    }

    public synchronized Jedis getJedisConnection() {
        try {
            if(jedisPool == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(1000);

                // 아래는 reliability 확보를 필요로 할때 , 테스트하며 조절해본다
                //config.setMaxIdle(10);
                //config.setMinIdle(1);
                //config.setMaxWaitMillis(30000);

                // 아래는 conf/Configure 에서 읽어오도록 수정됨
                jedisPool = new JedisPool(config
                        ,"52.78.46.226"
                        ,6379);
            }
            return jedisPool.getResource();
        } catch (JedisConnectionException e) {
            logger.error("Could not establish Redis connection.");

            throw e;
        }
    }

    public synchronized void close (Jedis resource) {
        if(jedisPool != null) {
            try {
                if(resource != null ) {
                    jedisPool.returnResource(resource);
                    resource = null;
                }
            }catch (JedisConnectionException e) {
                jedisPool.returnBrokenResource(resource);
                resource = null;
            } finally {
                if (resource != null)
                    jedisPool.returnResource(resource);
                resource = null;
            }
        }
    }
}