package com.xy.web.kafka.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kafka消息队列 消费者
 * 
 * @author xiongyan
 * @date 2016年10月13日 下午4:38:01
 */
public class KafkaConsumer {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	/**
	 * zookeeper 地址列表
	 */
	private String zookeeper;
	
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
	private ConsumerConnector consumer;
	
	/**
	 * 消息流
	 */
	private Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap;
	
	/**
	 * 初始化消费者
	 */
	public void init() {
		Properties props = new Properties();
        //zookeeper ip和端口号
        props.put("zookeeper.connect", zookeeper);
        //group 代表一个消费组
        props.put("group.id", group);
        //zk连接超时
        props.put("zookeeper.session.timeout.ms", "10000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");
        props.put("rebalance.backoff.ms", "2000");
        props.put("rebalance.max.retries", "10");
        //序列化类
        props.put("serializer.class", "kafka.serializer.StringEncoder");

        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        if (null != topicList) {
        	for (String topic : topicList) {
        		topicCountMap.put(topic, 1);
        	}
        }
        
        consumerMap = consumer.createMessageStreams(topicCountMap);
        logger.info("zookeeper【{}】，group【{}】，topicList【{}】， kafka消费者初始化成功！", zookeeper, group, topicList);
	}
	
	/**
	 * 关闭消费者连接
	 */
	public void close() {
		if (null != consumer) {
			consumer.shutdown();
			consumerMap = null;
			consumer = null;
			logger.info("zookeeper【{}】，group【{}】，topicList【{}】， kafka消费者关闭成功！", zookeeper, group, topicList);
		}
	}
	
	/**
	 * 接收消息
	 * 
	 * @param topic
	 * @param callback
	 */
	public void receive(final String topic, final KafkaCallback callback) {
		List<KafkaStream<byte[], byte[]>> kafkaStreamList = consumerMap.get(topic);
		if (null != kafkaStreamList) {
			for (KafkaStream<byte[], byte[]> kafkaStream : kafkaStreamList) {
				ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();
				while (iterator.hasNext()) {
					String message = new String(iterator.next().message());
					callback.receive(message);
				}
			}
		}
	}
	
	public String getZookeeper() {
		return zookeeper;
	}

	public void setZookeeper(String zookeeper) {
		this.zookeeper = zookeeper;
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
		final KafkaConsumer c = new KafkaConsumer();
		c.setZookeeper("192.168.11.101:2181,192.168.12.128:2181,192.168.12.154:2181/kafka");
		c.setGroup("group");
		c.setTopicList(Arrays.asList("bolt"));
		c.init();
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new Runnable() {
			public void run() {
				c.receive("bolt", new KafkaCallback() {
					public void receive(String message) {
						System.out.println("xxxxxxxxxxxxxxx------log-bolt------log-bolt:"+message);
					}
				});
			}
		});
		
	}
}
