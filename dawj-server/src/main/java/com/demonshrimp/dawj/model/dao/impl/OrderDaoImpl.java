package com.demonshrimp.dawj.model.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.demonshrimp.dawj.model.dao.OrderDao;
import com.demonshrimp.dawj.model.entity.Order;

@Repository
public class OrderDaoImpl extends BaseDaoImpl<Order, String> implements OrderDao {

	@Override
	public List<Object[]> statisticsQuantityMonthly(String siteId, int year) {
		String sql = "select count(*),substr(t.create_time,6,2)  from counseling_order t where t.site_id = ? and t.create_time like ? group by substr(t.create_time,1,7)";
		List<Object[]> list = createSQLQuery(sql, siteId, year + "%").list();
		return list;
	}

	
}