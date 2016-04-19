package com.demonshrimp.dawj.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.User;
import com.demonshrimp.dawj.service.UserService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.annotation.SerializationFilters;
import pers.ksy.common.model.Result;
import pers.ksy.common.orm.Conditions;
import pers.ksy.common.orm.MatchMode;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.QueryConditionImpl;

@RestController(value = "userAdminController")
@RequestMapping(value = "/admin/user")
public class UserController extends BaseAdminController {
	@Autowired
	private UserService userService;

	@RequestMapping(path = "/page", method = RequestMethod.GET)
	@SerializationFilter(target = User.class, fields = { "password" })
	public Object page(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "length") int pageSize,
			String keyword, String orderBy, boolean desc) {
		QueryCondition queryCondition = new QueryConditionImpl(User.class, null);
		if (null != keyword) {
			queryCondition.or(Conditions.like("name", keyword, MatchMode.ANYWHERE),
					Conditions.like("mobile", keyword, MatchMode.ANYWHERE));
		}
		return Result.successResult(userService.findByPage(queryCondition, pageIndex, pageSize), null);
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.GET)
	@SerializationFilters(filters = { @SerializationFilter(target = User.class, fields = { "password" }) })
	public Object get(@PathVariable String userId) {
		return Result.successResult(userService.get(userId), null);
	}

}
