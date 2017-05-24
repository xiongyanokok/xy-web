package com.xy.web.generator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Charsets;

/**
 * zookeeper
 * 
 * @author xiongyan
 * @date 2017年1月12日 下午3:49:38
 */
public class ZookeeperConfig implements InitializingBean, DisposableBean {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);

	/**
	 * zookeeper Framework
	 */
	private CuratorFramework client;
	
	/**
	 * zookeeper 分布式锁
	 */
	private InterProcessSemaphoreMutex lock;
	
	/**
     * 连接Zookeeper服务器的列表.
     * 包括IP地址和端口号.
     * 多个地址用逗号分隔.
     * 如: host1:2181,host2:2181
     */
    private String serverLists;
    
    /**
     * 命名空间.
     */
    private final static String NAMESPACE = "rgenerator";
    
    /**
     * 节点
     */
    private String path;
	
    
	public void setServerLists(String serverLists) {
		this.serverLists = serverLists;
	}

	/**
	 * 设置zookeeper节点
	 * 
	 * @param path
	 * @throws Exception
	 */
	public void setPath(String path) throws Exception {
		this.path = path;
		
		// 节点不存在创建此节点
		Stat stat = client.checkExists().forPath(path);
		if (null == stat) {
			// 创建节点
			client.createContainers(path);
		}
		
		// 共享锁
		lock = new InterProcessSemaphoreMutex(client, path + "_lock");
	}

	public void afterPropertiesSet() throws Exception {
		client = CuratorFrameworkFactory.builder()
                .connectString(serverLists)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3, 3000))
                .namespace(NAMESPACE)
                .build();

		client.start();
	}

	public void destroy() throws Exception {
		waitForCacheClose();
		CloseableUtils.closeQuietly(client);
	}
	
	/* TODO 等待500ms, cache先关闭再关闭client, 否则会抛异常
     * 因为异步处理, 可能会导致client先关闭而cache还未关闭结束.
     * 等待Curator新版本解决这个bug.
     * BUG地址：https://issues.apache.org/jira/browse/CURATOR-157
     */
    private void waitForCacheClose() {
        try {
            Thread.sleep(500L);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    
    /**
     * 节点赋值
     * 
     * @param bytes
     */
    public void setData(String data) {
    	try {
    		// 获得锁
    		lock.acquire();
    		
    		// 获取节点数据
    		String value = getData();
    		if (null == value || Long.parseLong(data) > Long.parseLong(value)) {
    			client.setData().forPath(path, data.getBytes(Charsets.UTF_8));
    		}
		} catch (Exception e) {
			logger.error("设置节点数据失败：", e);
		} finally {
			try {
				// 释放锁
				lock.release();
			} catch (Exception e) {
				logger.error("释放锁失败：", e);
			}
		}
    }

    /**
     * 获取节点内容
     * 
     * @return
     */
    public String getData() {
    	try {
    		byte[] data = client.getData().forPath(path);
    		if (null != data && data.length > 0) {
    			return new String(data, Charsets.UTF_8);
    		}
		} catch (Exception e) {
			logger.error("获取节点数据失败：", e);
		}
    	return null;
    }
	
}
