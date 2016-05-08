package com.demonshrimp.dawj.service;

import java.util.List;
import java.util.Map;

import com.demonshrimp.dawj.model.entity.DiscountRule;
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
	public void pay(String orderId,String thirdOrderId,Order.PaymentPlatform paymentPlatform);

	/**
	 * 完成订单
	 * @param orderId 订单ID
	 */
	public void complete(String orderId);
	
	/**
	 * 关闭订单
	 * @param orderId 订单ID
	 */
	public void close(String orderId);
	

	/**
	 * 退款申请
	 * @param orderId
	 */
	void refundApply(String orderId);


	/**
	 * 退款
	 * @param orderId
	 */
	void refund(String orderId);


	/**
	 * 年度订单量月统计
	 * @param year 年份
	 * @return
	 */
	public long[] statisticsQuantityMonthly(String siteId, int year);
	
	/**
	 * 检查订单是不是用户的
	 * @param orderId
	 * @param userId
	 */
	void selfOrderCheck(String orderId, String userId);


	/**
	 * 创建微信支付
	 * @param orderId
	 * @return 支付二维码链接
	 */
	String createWechatPayment(String orderId);


	/**
	 * 微信支付完成通知接口
	 * @param wechatResult
	 */
	public void wechatPayComplete(String wechatResult);


	/**
	 * 微信页面支付签名
	 * @param orderId 订单ID
	 * @return
	 */
	Map<String, String> getPaySignature(String orderId);


	List<DiscountRule> discountRuleList();


	void addDiscountRule(DiscountRule discountRule);


	void updateDiscountRule(DiscountRule discountRule);


	void delDiscountRule(String discountRuleId);


	public Object getDiscountRule(String discountRuleId);

}
