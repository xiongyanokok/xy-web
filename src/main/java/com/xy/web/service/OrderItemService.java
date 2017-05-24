package com.xy.web.service;

import java.util.List;
import java.util.Map;

import com.xy.web.model.OrderItem;

/**
 * Service 接口
 * 
 * @author admin
 * @date 2016年11月14日 下午5:59:57
 */
public interface OrderItemService {

    /**
     * 新增数据
     *
     * @param orderItem
     * @return 主键
     */
    public Integer insert(OrderItem orderItem);

    /**
     * 修改数据
     *
     * @param orderItem
     */
    public void update(OrderItem orderItem);
    
    /**
     * 删除数据
     * 
     * @param orderItemId
     */
    public void delete(Integer orderItemId);
    
    /**
     * 根据主键查询
     *
     * @param orderItemId
     * @return
     */
    public OrderItem selectByPrimaryKey(Integer orderItemId);
    
    /**
     * 根据map查询对象
     * 
     * @param map
     * @return
     */
    public OrderItem getOrderItem(Map<String, Object> map);
    
    /**
     * 根据map查询集合
     * 
     * @param map
     * @return
     */
    public List<OrderItem> queryOrderItemByMap(Map<String, Object> map);
    
}
