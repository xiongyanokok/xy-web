package com.xy.web.generator;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * id生成器
 * 
 * @author xiongyan
 * @date 2017年1月12日 上午10:51:16
 */
public class IdGenerator implements InitializingBean, DisposableBean {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(IdGenerator.class);

	/**
	 * 队列：1000个
	 */
	private ArrayBlockingQueue<Long> queue = null;
	
	/**
	 * 单线程池
	 */
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	private RedisConfig redisConfig;
	
	private ZookeeperConfig zookeeperConfig;
	
	private static final String BASE = "rgenerator";
	
	private int queueLength = 1000;
	
	private String business;
	
	private String key;

	
	public void setRedisConfig(RedisConfig redisConfig) {
		this.redisConfig = redisConfig;
	}

	public void setZookeeperConfig(ZookeeperConfig zookeeperConfig) {
		this.zookeeperConfig = zookeeperConfig;
	}
	
	public void setQueueLength(int queueLength) {
		this.queueLength = queueLength;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// zookeeper 中的节点
		zookeeperConfig.setPath("/" + business + "/" + key);
		
		// redis 中的key
		key = BASE + ":" + business + ":" + key;
		
		// 创建定长队列
		queue = new ArrayBlockingQueue<>(queueLength);
		
		// 预支数据
		put();
	}
	
	@Override
	public void destroy() throws Exception {
		executor.shutdown();
		
		queue.clear();
	}
	
	/**
	 * 从redis预支到队列中
	 */
	private void put() {
		if (!redisConfig.exists(key)) {
			String value = zookeeperConfig.getData();
			if (null != value) {
				// 给redis中重新赋值
				redisConfig.set(key, value);
			}
		}
		
		executor.execute(new Runnable() {
			public void run() {
				while (true) {
					if (queue.size() <= 10) {
						try {
							List<Object> ids = redisConfig.batchIncr(key);
							if (null != ids && !ids.isEmpty()) {
								for (Object id : ids) {
									try {
										queue.put(Long.valueOf(id.toString()));
									} catch (Exception e) {
									}
								}
							}
						} catch (Exception e) {
							logger.error("失败：", e);
						} finally {
							// 最大值寄存到zookeeper中
							zookeeperConfig.setData(redisConfig.get(key));
						}
					}
				}
			}
		});
	}
	
	/**
	 * 获取唯一id
	 * @return
	 */
	public Long getId() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			return null;
		}
	}

}
