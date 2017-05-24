package com.xy.web.service;

import java.util.List;
import java.util.Map;

import com.xy.web.model.Order;

/**
 * Service 接口
 * 
 * @author admin
 * @date 2016年11月14日 下午5:59:57
 */
public interface OrderService {

    /**
     * 新增数据
     *
     * @param order
     * @return 主键
     */
    public Integer insert(Order order);

    /**
     * 修改数据
     *
     * @param order
     */
    public void update(Order order);
    
    /**
     * 删除数据
     * 
     * @param orderId
     */
    public void delete(Integer orderId);
    
    /**
     * 根据主键查询
     *
     * @param orderId
     * @return
     */
    public Order selectByPrimaryKey(Integer orderId);
    
    void saveO();
    
    /**
     * 根据map查询对象
     * 
     * @param map
     * @return
     */
    public Order getOrder(Map<String, Object> map);
    
    /**
     * 根据map查询集合
     * 
     * @param map
     * @return
     */
    public List<Order> queryOrderByMap(Map<String, Object> map);
    
    public Integer batchInsert(List<Order> orders);
    
}
