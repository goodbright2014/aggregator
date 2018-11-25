package com.skt.ehs.mbs.db.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skt.ehs.mbs.conf.Configure;
import com.skt.ehs.mbs.module.mct.lc.APDataProcessor;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis의 Connection Pool을 관리해 주는 역할을 수행
 * @author YiSuHwan
 *
 */
public class RedisConnectionManager {
	final Logger logger = LoggerFactory.getLogger(RedisConnectionManager.class);
	
	private static RedisConnectionManager mInstance;
	
	/**
	 * Redis에 접근한 Connection Pool을 저장함
	 */
	private JedisPool mPool;



	public static RedisConnectionManager getInstance() {
		if (mInstance == null)
			mInstance = new RedisConnectionManager();
		return mInstance;
	}

	private RedisConnectionManager() {
		logger.info("Make Redis Connection Pool Start ");
		boolean ret = makeConnectionPool();
		logger.info("Make Redis Connection Pool End " + ret + ":" + mPool.getNumIdle());
	}
	
	/**
	 * 
	 * @return true : 정상 접속
	 * 			false : pool을 만들기 실패 서버 구동시에 지속적인 문제 발생할수 있음. 수집 관련된 처리가 안될 것임 
	 */
	public synchronized boolean makeConnectionPool() {

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		//jedisPoolConfig.setMaxTotal(APDataProcessor.MAX_THREAD_NUMBER); // default 8 : ZeroMQ쪽에서 생성되는 Thread를 Max 만큼 잡아주었다.
		//jedisPoolConfig.setMaxTotal(2);
		//jedisPoolConfig.setBlockWhenExhausted(true); // default true
		//jedisPoolConfig.setMaxTotal(1000);

		try {

			//JedisPool pool = new JedisPool(jedisPoolConfig
			//		, Configure.MBS_TEMP_INFO_REDIS_IP
			//		, Configure.MBS_TEMP_INFO_REDIS_PORT);
			JedisPool pool = new JedisPool(jedisPoolConfig, "52.78.46.226",6379);
			mPool = pool;
		} catch (Exception e) {
			return false;
		}
		
		if (mPool.isClosed() == true)
			return false;
		
		return true;
	}
	
	/**
	 * Jedis의 Connection을 획득한다.
	 * @return
	 * return a jedis connection
	 */
	public Jedis getConnection() {
		if (mPool == null) {
			logger.error("jedis pool is out of control..............");
			return null;
		}
		try {
			//
			return mPool.getResource();

		}catch (Exception e) {
			logger.debug(" : " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Connection을 반납한다.
	 * @param jedis
	 */
	public void returnConnection(Jedis jedis) {
		if (jedis != null)
			jedis.close();
	}
}
