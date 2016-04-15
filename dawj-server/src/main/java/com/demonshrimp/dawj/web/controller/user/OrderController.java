package com.demonshrimp.dawj.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.Order;
import com.demonshrimp.dawj.service.OrderService;

import pers.ksy.common.StringUtil;
import pers.ksy.common.model.Result;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.QueryConditionImpl;

@RestController(value = "orderUserController")
@RequestMapping(value = "/usr/order")
public class OrderController extends BaseUserController {
	@Autowired
	private OrderService orderService;

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Object save(@RequestBody Order order) {
		order.setUser(getCurrentUser());
		order = orderService.addOrder(order, getCurrentSite().getId());
		return Result.successResult(order.getId(), "订单提交成功");
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
	public Object update(@PathVariable String orderId, @RequestBody Order order) {
		order.setId(orderId);
		orderService.update(order);
		return Result.successResult("更新成功");
	}

	@RequestMapping(path = "/page", method = RequestMethod.GET)
	public Object page(@RequestParam(name = "start") int pageIndex, @RequestParam(name = "length") int pageSize,
			String orderBy, @RequestParam(defaultValue = "false") Boolean desc) {
		QueryCondition qc = new QueryConditionImpl(Order.class);
		qc.eq("site.id", getCurrentSite().getId());
		qc.eq("user.id", getCurrentUser().getId());
		if (StringUtil.notEmpty(orderBy)) {
			qc.addOrder(pers.ksy.common.orm.Order.add(orderBy, !desc));
		}
		//qc.addOrder(pers.ksy.common.orm.Order.desc("createTime"));
		return Result.successResult(orderService.findByPage(qc, pageIndex, pageSize), null);
	}

}
