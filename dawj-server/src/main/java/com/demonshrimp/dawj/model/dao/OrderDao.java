package com.demonshrimp.dawj.model.dao;

import java.util.List;

import com.demonshrimp.dawj.model.entity.Order;

public interface OrderDao extends BaseDao<Order, String> {

	List<Object[]> statisticsQuantityMonthly(String siteId, int year);
}