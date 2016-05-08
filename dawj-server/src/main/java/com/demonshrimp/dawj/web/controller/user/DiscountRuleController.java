package com.demonshrimp.dawj.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.service.OrderService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;

@RestController(value = "discountRuleUserController")
@RequestMapping(value = "/usr/discount-rule")
public class DiscountRuleController extends BaseUserController {
	@Autowired
	private OrderService orderService;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	@SerializationFilter
	public Object list() {
		return Result.successResult(orderService.discountRuleList(),null);
	}

}
