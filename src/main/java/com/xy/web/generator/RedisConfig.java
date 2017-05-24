package com.xy.web.generator;

import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.util.Pool;

/**
 * redis
 * 
 * @author xiongyan
 * @date 2017年1月12日 下午12:12:03
 */
public class RedisConfig implements InitializingBean, DisposableBean {

	private Pool<Jedis> redisPool;
	
	private int BATCHNUM = 1000; 
	
	private Jedis jedis;
	

	public void setRedisPool(Pool<Jedis> redisPool) {
		this.redisPool = redisPool;
	}

	public void afterPropertiesSet() throws Exception {
		jedis = redisPool.getResource();
	}
	
	public void destroy() throws Exception {
		redisPool.destroy();
		
		jedis.close();
	}
	
	public List<Object> batchIncr(String key) {
		return batchIncr(key, BATCHNUM);
	}
	
	public List<Object> batchIncr(String key, int batchNum) {
		Pipeline pipeline = jedis.pipelined();
		for (int i = 0; i < batchNum; i++) {
			pipeline.incr(key);
		}
		return pipeline.syncAndReturnAll();
	}
	
	public void set(String key, String value) {
		jedis.set(key, value);
	}
	
	public String get(String key) {
		return jedis.get(key);
	}
	
	public Boolean exists(String key) {
		return jedis.exists(key);
	}
}
