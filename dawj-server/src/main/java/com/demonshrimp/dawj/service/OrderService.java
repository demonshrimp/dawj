package com.demonshrimp.dawj.service;

import com.demonshrimp.dawj.model.entity.Order;

public interface OrderService extends BaseService<Order,String>{
	/**
	 * 在某站点下新增一个用户
	 * @param order
	 * @param orgId
	 * @return
	 */
	public Order addOrder(Order order, String siteId);
	
	
	/**
	 * 更新用户
	 */
	public Order update(Order user);
	
	/**
	 * 逻辑删除用户
	 * @param user
	 * @return
	 */
	public Order delete(String id);	

	/**
	 * 支付订单
	 * @param orderId 订单ID
	 */
	public void pay(String orderId);

	/**
	 * 完成订单
	 * @param orderId 订单ID
	 */
	public void complete(String orderId);
	
	/**
	 * 结束订单
	 * @param orderId 订单ID
	 */
	public void close(String orderId);
}
