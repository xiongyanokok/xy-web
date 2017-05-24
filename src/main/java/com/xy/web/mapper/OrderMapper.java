package com.xy.web.mapper;

import java.util.List;
import java.util.Map;

import com.xy.web.model.Order;

/**
 * Mapper
 * 
 * @author admin
 * @date 2016年11月14日 下午5:28:30
 */
public interface OrderMapper {

	/**
     * 根据主键查询
     *
     * @param id
     */
    Order selectByPrimaryKey(Integer id);

    /**
     * 新增数据库记录
     *
     * @param order
     * @return 主键
     */
    Integer insert(Order order);

    /**
     * 动态字段，新增数据库记录
     *
     * @param order
     * @return 主键
     */
    Integer insertSelective(Order order);

    /**
     * 根据主键动态更新数据库记录
     *
     * @param order
     */
    void updateByPrimaryKeySelective(Order order);

    /**
     * 根据map查询对象
     * 
     * @param map
     * @return
     */
    Order getOrder(Map<String, Object> map);
    
    /**
     * 根据map查询集合
     * 
     * @param map
     * @return
     */
    List<Order> queryOrderByMap(Map<String, Object> map);
    
    Integer batchInsert(List<Order> orders);
    
}
