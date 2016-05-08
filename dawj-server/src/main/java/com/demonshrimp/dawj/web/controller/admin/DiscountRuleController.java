package com.demonshrimp.dawj.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.DiscountRule;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.OrderService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;

@RestController(value = "discountRuleAdminController")
@RequestMapping(value = "/admin/discount-rule")
public class DiscountRuleController extends BaseAdminController {
	@Autowired
	private OrderService orderService;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	@SerializationFilter
	public Object list() {
		return Result.successResult(orderService.discountRuleList(),null);
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Object save(@RequestBody DiscountRule discountRule) {
		orderService.addDiscountRule(discountRule);
		return Result.successResult(discountRule.getId(), "新增成功");
	}

	@SerializationFilter
	@RequestMapping(path = "/{discountRuleId}", method = RequestMethod.GET)
	public Object get(@PathVariable String discountRuleId) {
		return Result.successResult(orderService.getDiscountRule(discountRuleId), null);
	}

	@RequestMapping(path = "/{discountRuleId}", method = RequestMethod.PUT)
	public Object update(@PathVariable String discountRuleId, @RequestBody DiscountRule discountRule) {
		discountRule.setId(discountRuleId);
		orderService.updateDiscountRule(discountRule);
		return Result.successResult("更新成功");
	}

	@RequestMapping(path = "/{discountRuleId}", method = RequestMethod.DELETE)
	public Object delete(@PathVariable String discountRuleId) {
		orderService.delDiscountRule(discountRuleId);
		return Result.successResult("删除成功");
	}

}
