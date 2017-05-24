package com.xy.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * controller的基类
 * 
 * @author xiongyan
 * @date 2016年10月27日 下午2:06:29
 */
public abstract class BaseController {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
    protected HttpServletRequest request;
	
	@Autowired
	protected HttpServletResponse response;
	
	/**
	 * 构建成功消息
	 * 
	 * @param obj
	 * @return
	 */
	public Map<String, Object> buildSuccess(Object obj) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", 'Y');
		map.put("message", obj);
		return map;
	}
	
	/**
	 * 构建失败消息
	 * 
	 * @param obj
	 * @return
	 */
	public Map<String, Object> buildFail(Object obj) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", 'N');
		map.put("message", obj);
		return map;
	}
	
}
