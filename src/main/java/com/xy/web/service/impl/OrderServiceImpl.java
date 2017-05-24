package com.xy.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xy.web.mapper.OrderMapper;
import com.xy.web.model.Order;
import com.xy.web.service.OrderService;

/**
 * Service 实现
 * 
 * @author admin
 * @date 2016年11月14日 下午5:59:57
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
	private OrderMapper orderMapper;
    
    @Resource(name="orderServiceImpl")
	private OrderService orderService;
	
	/**
     * 新增数据
     *
     * @param order
     * @return 主键
     */
    public Integer insert(Order order) {
    	if (null == order) {
    		return null;
    	} else {
    		orderMapper.insert(order);
    		//int i=1/0;
    		return 1;
    	}
    }

    /**
     * 修改数据
     *
     * @param order
     */
    public void update(Order order) {
    	if (null != order) {
    		orderMapper.updateByPrimaryKeySelective(order);
    	}
    }
    
    /**
     * 删除数据
     * 
     * @param orderId
     */
    public void delete(Integer orderId) {
    	if (null != orderId && orderId > 0) {
    		Order order = new Order();
    		order.setOrderId(orderId);
    		orderMapper.updateByPrimaryKeySelective(order);
    	}
    }
    
    /**
     * 根据主键查询
     *
     * @param orderId
     * @return
     */
    public Order selectByPrimaryKey(Integer orderId) {
    	if (null == orderId || orderId <= 0) {
    		return null;
    	}
    	return orderMapper.selectByPrimaryKey(orderId);
    }
    
    /**
     * 根据map查询对象
     * 
     * @param map
     * @return
     */
    public Order getOrder(Map<String, Object> map) {
    	if (null == map || map.isEmpty()) {
			return null;
		}
    	return orderMapper.getOrder(map);
    }
    
    public void saveO() {
    	Map<String, Object> map = new HashMap<>();
		map.put("userId", 1);
		orderService.queryOrderByMap(map);
    }
    
    /**
     * 根据map查询集合
     * 
     * @param map
     * @return
     */
    public List<Order> queryOrderByMap(Map<String, Object> map) {
    	if (null == map || map.isEmpty()) {
			return null;
		}
    	/*Order order = new Order();
    	order.setOrderId(5);
    	order.setUserId(1);
    	this.insert(order);*/
    	return orderMapper.queryOrderByMap(map);
    }
    
    
    public Integer batchInsert(List<Order> orders) {
    	return orderMapper.batchInsert(orders);
    }
}
