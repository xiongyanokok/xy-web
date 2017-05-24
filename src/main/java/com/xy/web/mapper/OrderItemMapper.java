package com.xy.web.mapper;

import java.util.List;
import java.util.Map;

import com.xy.web.model.OrderItem;

/**
 * Mapper
 * 
 * @author admin
 * @date 2016年11月14日 下午5:28:30
 */
public interface OrderItemMapper {

	/**
     * 根据主键查询
     *
     * @param id
     */
    OrderItem selectByPrimaryKey(Integer id);

    /**
     * 新增数据库记录
     *
     * @param orderItem
     * @return 主键
     */
    Integer insert(OrderItem orderItem);

    /**
     * 动态字段，新增数据库记录
     *
     * @param orderItem
     * @return 主键
     */
    Integer insertSelective(OrderItem orderItem);

    /**
     * 根据主键动态更新数据库记录
     *
     * @param orderItem
     */
    void updateByPrimaryKeySelective(OrderItem orderItem);

    /**
     * 根据map查询对象
     * 
     * @param map
     * @return
     */
    OrderItem getOrderItem(Map<String, Object> map);
    
    /**
     * 根据map查询集合
     * 
     * @param map
     * @return
     */
    List<OrderItem> queryOrderItemByMap(Map<String, Object> map);
    
}
