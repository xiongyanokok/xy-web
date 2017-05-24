package com.xy.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xy.web.model.Order;
import com.xy.web.service.OrderService;

@Controller
@RequestMapping(value = "/order", produces = {"application/json; charset=UTF-8"})
public class OrderController extends BaseController {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @RequestMapping(value = "/mongodb", method = {RequestMethod.GET})
    @ResponseBody
	public String mongodb(Model model) {
    	mongoTemplate.getDb();
    	return "mongodb";
    }
    
	/**
	 * 列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET})
	public String list(Model model) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", 1);
		List<Order> orderList =  orderService.queryOrderByMap(map);	
		for (Order order : orderList) {
			logger.info("{}--{}", order.getOrderId(), order.getUserId());
		}
		//orderService.saveO();
		return "order/list";
	}
	
	/**
	 * 列表分页查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/query", method = {RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> query(Integer userId) {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("userId", userId);
			// 设置分页参数
			PageHelper.startPage(1, 10);
			// 排序
			PageHelper.orderBy("updateTime desc");
			// 查询
			List<Order> orderList =  orderService.queryOrderByMap(map);			
			// 分页
			PageInfo<Order> pageInfo = new PageInfo<>(orderList);
			return buildSuccess(pageInfo.getList());
		} catch (Exception e) {
			logger.error("列表分页查询失败：", e);
			return buildFail("查询失败");
		}
	}
	
	/**
	 * 保存数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/save", method = {RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> save() {
		try {
			for (int i = 1; i <= 100; i++) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						String name = Thread.currentThread().getName();
						int n = Integer.parseInt(name);
						for (int j = (n - 1) * 100 + 1; j <= 100 * n; j++) {
							Order order = new Order();
							order.setUserId(n);
							order.setOrderId(j);
							orderService.insert(order);
						}
					}
				}, String.valueOf(i));
				t.start();
				Thread.sleep(5000);
			}
			return buildSuccess("保存成功");
		} catch (Exception e) {
			logger.error("保存失败：", e);
			return buildFail("保存失败");
		}
	}
	
	/**
	 * 保存数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/batch", method = {RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> batch() {
		try {
			List<Order> orderList = new ArrayList<>();
			for (int i = 101; i <= 110; i++) {
				int n = i;
				for (int j = (n - 1) * 100 + 1; j <= 100 * n; j++) {
					Order order = new Order();
					order.setUserId(n);
					order.setOrderId(j);
					orderList.add(order);
				}
			}
			orderService.batchInsert(orderList);
			return buildSuccess("保存成功");
		} catch (Exception e) {
			logger.error("保存失败：", e);
			return buildFail("保存失败");
		}
	}
	
	/**
	 * 删除数据
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> delete(@RequestParam(value = "orderId", required = true) Integer orderId) {
		try {
			orderService.delete(orderId);
			return buildSuccess("删除成功");
		} catch (Exception e) {
			logger.error("删除失败：", e);
			return buildFail("删除失败");
		}
	}
	
}
