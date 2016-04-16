package com.demonshrimp.dawj.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.OrderDao;
import com.demonshrimp.dawj.model.entity.Order;
import com.demonshrimp.dawj.model.entity.Order.PaymentPlatform;
import com.demonshrimp.dawj.model.entity.Order.Status;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.OrderService;

@Transactional
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, String> implements OrderService {
	@Autowired
	private OrderDao orderDao;

	/*@Autowired
	private AlipayClient alipayClient;*/

	@Override
	public Order addOrder(Order order, String siteId) {
		order.setSite(new Site(siteId));
		order.setStatus(Status.NEW);
		return save(order);
	}

	@Override
	public Order delete(String id) {
		throw new ServiceException("not implement!");
	}

	@Override
	public void pay(String orderId, String thirdOrderId, PaymentPlatform paymentPlatform) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.NEW) {
			throw new ServiceException("不能对该状态的订单进行付款操作");
		}
		order.setThirdOrderId(thirdOrderId);
		order.setPaymentPlatform(paymentPlatform);
		order.setPaymentTime(new Date());
		order.setStatus(Order.Status.PAID);
		orderDao.update(order);
	}

	@Override
	public void complete(String orderId) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.PAID) {
			throw new ServiceException("不能对该状态的订单进行完成操作");
		}
		order.setFulfillmentTime(new Date());
		order.setStatus(Order.Status.COMPLETED);
		orderDao.update(order);
	}

	@Override
	public void close(String orderId) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.NEW) {
			throw new ServiceException("不能对该状态的订单进行关闭操作");
		}
		order.setCloseTime(new Date());
		order.setStatus(Order.Status.CLOSED);
		orderDao.update(order);
	}

	@Override
	public void refundApply(String orderId) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.PAID) {
			throw new ServiceException("不能对该状态的订单进行退款申请操作");
		}
		order.setRefundApplyTime(new Date());
		order.setStatus(Order.Status.REFUNDABLE);
		orderDao.update(order);
	}
	
	@Override
	public void refund(String orderId) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.REFUNDABLE) {
			throw new ServiceException("不能对该状态的订单进行退款操作");
		}
		/*switch (order.getPaymentPlatform()) {
		case ALIPAY:
			refundFromAlipay(order);
			break;
		default:
			throw new ServiceException(order.getPaymentPlatform() + "平台暂不支持退款操作");
		}*/
		order.setCloseTime(new Date());
		order.setStatus(Order.Status.CLOSED);
		orderDao.update(order);
	}

	@Override
	public long[] statisticsQuantityMonthly(String siteId, int year) {
		long[] result = new long[12];
		List<Object[]> list = orderDao.statisticsQuantityMonthly(siteId, year);
		for (Object[] data : list) {
			long number = Long.valueOf(data[0].toString());
			int month = Integer.valueOf(data[1].toString());
			result[month - 1] = number;
		}
		return result;
	}
	

	@Override
	public void selfOrderCheck(String orderId, String userId) {
		Order order = orderDao.load(orderId);
		if(order.getUser().getId()!=userId){
			throw new ServiceException("非法操作其他订单");
		}
	}

	@Override
	protected BaseDao<Order, String> getDao() {
		return orderDao;
	}

	// state methods

	/**
	 * 支付宝退款
	 * 
	 * @throws AlipayApiException
	 */
	/*private void refundFromAlipay(Order order) {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("out_trade_no", order.getId());
		jsonObject.addProperty("trade_no", order.getThirdOrderId());
		jsonObject.addProperty("refund_amount", order.getAmount());
		request.setBizContent(jsonObject.toString());
		try {
			AlipayTradeRefundResponse response = alipayClient.execute(request);
			JsonObject responseJson = new Gson().fromJson(response.getBody(), JsonObject.class);
			responseJson = responseJson.getAsJsonObject("alipay_trade_refund_response");
			System.out.println(responseJson);
			if (responseJson.get("code").getAsString() != "10000") {
				throw new ServiceException("支付宝退款失败：" + responseJson.get("msg") + responseJson.get("sub_msg"));
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			throw new ServiceException("支付宝退款失败：" + e.getMessage());
		}
	}*/
}
