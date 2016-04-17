package com.demonshrimp.dawj.web.controller.admin;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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

@RestController(value = "orderAdminController")
@RequestMapping(value = "/admin/order")
public class OrderController extends BaseAdminController {
	@Autowired
	private OrderService orderService;

	@RequestMapping(path = "/page", method = RequestMethod.GET)
	public Object page(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "length") int pageSize,
			String orderBy, @RequestParam(defaultValue = "false") Boolean desc) {
		QueryCondition qc = new QueryConditionImpl(Order.class);
		qc.eq("site.id", getCurrentSite().getId());
		if (StringUtil.notEmpty(orderBy)) {
			qc.addOrder(pers.ksy.common.orm.Order.add(orderBy, !desc));
		}
		// qc.addOrder(pers.ksy.common.orm.Order.desc("createTime"));
		return Result.successResult(orderService.findByPage(qc, pageIndex, pageSize), null);
	}

	@RequestMapping(path = "/refund-page", method = RequestMethod.GET)
	public Object refundPage(@RequestParam(name = "start") int pageIndex, @RequestParam(name = "length") int pageSize,
			String orderBy, @RequestParam(defaultValue = "false") Boolean desc) {
		QueryCondition qc = new QueryConditionImpl(Order.class);
		qc.eq("site.id", getCurrentSite().getId());
		qc.eq("status", Order.Status.REFUNDABLE);
		if (StringUtil.notEmpty(orderBy)) {
			qc.addOrder(pers.ksy.common.orm.Order.add(orderBy, !desc));
		}
		// qc.addOrder(pers.ksy.common.orm.Order.desc("createTime"));
		return Result.successResult(orderService.findByPage(qc, pageIndex, pageSize), null);
	}

	@RequestMapping(path = "/{orderId}/close", method = RequestMethod.PUT)
	public Object close(@PathVariable String orderId) {
		orderService.close(orderId);
		return Result.successResult("订单关闭成功!");
	}

	@RequestMapping(path = "/{orderId}/complete", method = RequestMethod.PUT)
	public Object complete(@PathVariable String orderId) {
		orderService.complete(orderId);
		return Result.successResult("订单完成成功!");
	}

	@RequestMapping(path = "/{orderId}/refund", method = RequestMethod.PUT)
	public Object refund(@PathVariable String orderId) {
		orderService.refund(orderId);
		return Result.successResult("订单退款成功!");
	}

	@RequestMapping(path = "/count", method = RequestMethod.GET)
	public Object count() {
		QueryCondition qc = new QueryConditionImpl(Order.class);
		Long count = orderService.count(qc);
		return Result.successResult(count, null);
	}

	@RequestMapping(path = "/statistics-quantity-monthly", method = RequestMethod.GET)
	public Object statisticsQuantityMonthly() {
		int year = new Date().getYear() + 1900;
		long[] data = orderService.statisticsQuantityMonthly(getCurrentSite().getId(), year);
		return Result.successResult(data, null);
	}

	@RequestMapping(path = "/wechat-pay-callback", method = { RequestMethod.GET, RequestMethod.POST })
	public void wechatPayCallback(HttpServletRequest request, @RequestBody(required = false) String wechatResult) {
		System.out.println(wechatResult);
		orderService.wechatPayComplete(wechatResult);
	}
}
