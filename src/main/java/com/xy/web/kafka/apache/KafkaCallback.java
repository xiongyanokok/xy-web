package com.xy.web.kafka.apache;

/**
 * kafka队列回调
 * 
 * @author xiongyan
 * @date 2016年10月13日 下午6:07:44
 */
public interface KafkaCallback {

	/**
	 * 接收消息
	 * 
	 * @param topic
	 * @param message
	 */
	public void receive(String topic, String message);
}
