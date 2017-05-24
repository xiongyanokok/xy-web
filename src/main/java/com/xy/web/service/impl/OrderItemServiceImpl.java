package com.xy.web.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xy.web.mapper.OrderItemMapper;
import com.xy.web.model.OrderItem;
import com.xy.web.service.OrderItemService;

/**
 * Service 实现
 * 
 * @author admin
 * @date 2016年11月14日 下午5:59:57
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
	private OrderItemMapper orderItemMapper;
	
	/**
     * 新增数据
     *
     * @param orderItem
     * @return 主键
     */
    public Integer insert(OrderItem orderItem) {
    	if (null == orderItem) {
    		return null;
    	} else {
    		return orderItemMapper.insert(orderItem);
    	}
    }

    /**
     * 修改数据
     *
     * @param orderItem
     */
    public void update(OrderItem orderItem) {
    	if (null != orderItem) {
    		orderItemMapper.updateByPrimaryKeySelective(orderItem);
    	}
    }
    
    /**
     * 删除数据
     * 
     * @param orderItemId
     */
    public void delete(Integer orderItemId) {
    	if (null != orderItemId && orderItemId > 0) {
    		OrderItem orderItem = new OrderItem();
    		orderItemMapper.updateByPrimaryKeySelective(orderItem);
    	}
    }
    
    /**
     * 根据主键查询
     *
     * @param orderItemId
     * @return
     */
    public OrderItem selectByPrimaryKey(Integer orderItemId) {
    	if (null == orderItemId || orderItemId <= 0) {
    		return null;
    	}
    	return orderItemMapper.selectByPrimaryKey(orderItemId);
    }
    
    /**
     * 根据map查询对象
     * 
     * @param map
     * @return
     */
    public OrderItem getOrderItem(Map<String, Object> map) {
    	if (null == map || map.isEmpty()) {
			return null;
		}
    	return orderItemMapper.getOrderItem(map);
    }
    
    /**
     * 根据map查询集合
     * 
     * @param map
     * @return
     */
    public List<OrderItem> queryOrderItemByMap(Map<String, Object> map) {
    	if (null == map || map.isEmpty()) {
			return null;
		}
    	return orderItemMapper.queryOrderItemByMap(map);
    }
    
}
