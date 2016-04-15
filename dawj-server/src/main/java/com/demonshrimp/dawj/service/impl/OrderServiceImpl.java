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
			throw new ServiceException("订单状态异常");
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
			throw new ServiceException("不能完成该状态的订单");
		}
		order.setFulfillmentTime(new Date());
		order.setStatus(Order.Status.COMPLETED);
		orderDao.update(order);
	}

	@Override
	public void close(String orderId) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.NEW) {
			throw new ServiceException("不能关闭该状态的订单");
		}
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
	protected BaseDao<Order, String> getDao() {
		return orderDao;
	}

}
