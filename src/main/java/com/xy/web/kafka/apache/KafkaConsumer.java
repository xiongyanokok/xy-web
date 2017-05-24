package com.xy.web.kafka.apache;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kafka消息队列 消费者
 * 
 * @author xiongyan
 * @date 2016年10月13日 下午4:38:01
 */
public class KafkaConsumer implements Serializable {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	/**
	 * kafka 服务器地址
	 */
	private String servers;
	
	/**
	 * 消费组
	 */
	private String group;
	
	/**
	 * 主题列表
	 */
	private List<String> topicList;
	
	/**
	 * 消费者
	 */
	private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;
	
	/**
	 * 初始化消费者
	 */
	public void init() {
		Properties props = new Properties();
		// kafka 服务器地址
		props.put("bootstrap.servers", servers);
		// group
		props.put("group.id", group);
		// consumer向zookeeper提交offset，定期提交
		props.put("enable.auto.commit", "true");
		// consumer向zookeeper提交offset的频率（单位毫秒，默认60*1000）
		props.put("auto.commit.interval.ms", "1000");
		// 心跳检测时间（单位毫秒，默认6000）
		props.put("session.timeout.ms", "30000");
		// key序列化
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		// value序列化
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		// 初始化kafka连接
		consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(props);
		// 订阅top
		consumer.subscribe(topicList);
        
        logger.info("kafka servers【{}】，group【{}】，topicList【{}】， kafka消费者初始化成功！", servers, group, topicList);
	}
	
	/**
	 * 关闭消费者连接
	 */
	public void close() {
		if (null != consumer) {
			consumer.close();
			consumer = null;
			logger.info("kafka servers【{}】，group【{}】，topicList【{}】， kafka消费者关闭成功！", servers, group, topicList);
		}
	}
	
	/**
	 * 接收消息
	 * 
	 * @param callback
	 */
	public void receive(KafkaCallback callback) {
		ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : records) {
        	System.out.println(record.value());
        	//callback.receive(record.topic(), record.value());
            //logger.info("Received message: ({}, {}) at offset({})", record.topic(), record.value(), record.offset());
        }
	}
	
	
	public String getServers() {
		return servers;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<String> getTopicList() {
		return topicList;
	}

	public void setTopicList(List<String> topicList) {
		this.topicList = topicList;
	}
	
	public static void main(String[] args) {
		final KafkaConsumer consumer = new KafkaConsumer();
		consumer.setServers("192.168.11.101:9092,192.168.12.128:9092,192.168.12.154:9092");
		consumer.setGroup("log-group");
		consumer.setTopicList(Arrays.asList("log"));
		consumer.init();
		
		
		while(true) {
			consumer.receive(new KafkaCallback() {
				public void receive(String topic, String message) {
					System.out.println(topic+"--"+message);
				}
			});
		}
		
		/*
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new Runnable() {
			public void run() {
				consumer.receive("match_stock_buy", new KafkaCallback() {
					public void receive(String message) {
						System.out.println("xxxxxxxxxxxxxxx------------1:"+message);
					}
				});
			}
		});
		
		executor.execute(new Runnable() {
			public void run() {
				consumer.receive("match_stock_sell", new KafkaCallback() {
					public void receive(String message) {
						System.out.println("xxxxxxxxxxxxxxx------------2:"+message);
					}
				});
			}
		});*/
	}
}
