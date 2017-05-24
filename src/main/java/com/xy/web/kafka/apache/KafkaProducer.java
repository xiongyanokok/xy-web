package com.xy.web.kafka.apache;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kafka消息队列 生产者
 * 
 * @author xiongyan
 * @date 2016年10月13日 下午4:38:01
 */
public class KafkaProducer {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

	/**
	 * kafka 服务器地址
	 */
	private String servers;
	
	/**
	 * 生产者
	 */
	private org.apache.kafka.clients.producer.KafkaProducer<String, String> producer;
	
	/**
	 * 初始化生产者
	 */
	public void init() {
		Properties props = new Properties();
		// kafka 服务器地址
		props.put("bootstrap.servers", servers);
		// kafka服务接收到数据之后发出的确认接收的信号 （默认为1）  
		// acks=0 表示：不需要等待任何确认收到的信息
		// acks=1 表示：leader已经成功将数据写入本地log
		// acks=-1（all） 表示：leader需要等待所有备份都成功写入日志（主备都成功写入）
		props.put("acks", "1");
		// 发送失败自动重试次数（默认0）
		props.put("retries", "0");
		// 每个分区的缓存区大小（默认16384）
		props.put("batch.size", "16384");
		// 延迟发送时间单位毫秒（默认0ms）
		props.put("linger.ms", "1");
		// 缓存总量（默认33554432）
		props.put("buffer.memory", "33554432");
		// key序列化
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		// value序列化
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		// 初始化kafka连接
		producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(props);

		logger.info("kafka servers【{}】， kafka生产者初始化成功！", servers);
	}
	
	/**
	 * 关闭生产者连接
	 */
	public void close() {
		if (null != producer) {
			producer.close();
			producer = null;
			logger.info("kafka servers【{}】， kafka生产者关闭成功！", servers);
		}
	}
	
	/**
	 * 发送消息
	 * 
	 * @param topic
	 * @param value
	 */
	public void send(String topic, String value) {
		producer.send(new ProducerRecord<String, String>(topic, value));
	}
	
	/**
	 * 发送消息，打印发送结果
	 * 
	 * @param topic
	 * @param value
	 */
	public void sendCallback(String topic, String value) {
		producer.send(new ProducerRecord<String, String>(topic, value), new Callback() {
			
			public void onCompletion(RecordMetadata metadata, Exception exception) {
				if (metadata != null) {
					logger.info("message({}, {})，sent to partition({})，offset({})", topic, value, metadata.partition(), metadata.offset());
		        }
				if (null != exception) {
					logger.error("message({}, {})，发送失败：", topic, value, exception);
				}
			}
		});
	}
	
	public String getServers() {
		return servers;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}

	public static void main(String[] args) {
		KafkaProducer producer = new KafkaProducer();
		producer.setServers("192.168.11.101:9092,192.168.12.128:9092,192.168.12.154:9092");
		producer.init();
		
		/*for(int i=0; i<100; i++) {
			producer.send("match_stock_buy", "熊焱测试新大赛使用kafka队列1xxx");
			producer.send("match_stock_sell", "熊焱测试新大赛使用kafka队列2yy");
		}*/
		for(int i=0; i<1000; i++) {
			producer.send("log", "测试kafka-jstorm="+i);
		}
		
		producer.close();
	}
}
