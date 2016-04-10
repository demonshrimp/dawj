package com.demonshrimp.dawj.model.dao.impl;

import org.springframework.stereotype.Repository;

import com.demonshrimp.dawj.model.dao.OrderDao;
import com.demonshrimp.dawj.model.entity.Order;

@Repository
public class OrderDaoImpl extends BaseDaoImpl<Order, String> implements OrderDao {

}