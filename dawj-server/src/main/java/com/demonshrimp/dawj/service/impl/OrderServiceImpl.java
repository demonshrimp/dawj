package com.demonshrimp.dawj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.OrderDao;
import com.demonshrimp.dawj.model.entity.Order;
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
	public void pay(String orderId) {
		throw new ServiceException("not implement!");
	}

	@Override
	public void complete(String orderId) {
		throw new ServiceException("not implement!");
	}

	@Override
	public void close(String orderId) {
		throw new ServiceException("not implement!");
	}

	@Override
	protected BaseDao<Order, String> getDao() {
		return orderDao;
	}

}
