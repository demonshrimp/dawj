package com.demonshrimp.dawj.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

@RestController
@RequestMapping(value = "/admin/user")
public class UserController extends BaseAdminController {
	@Autowired
	private UserService userService;

	@RequestMapping(path = "/page", method = RequestMethod.GET)
	@SerializationFilter(target = User.class, fields = { "password" })
	public Object page(String organizationId, String name, int pageIndex, int pageSize) {
		QueryCondition queryCondition = new QueryConditionImpl(User.class, null);
		if (null != organizationId) {
			queryCondition.add(Conditions.eq("organization.id", organizationId));
		}
		if (null != name) {
			queryCondition.add(Conditions.like("name", name, MatchMode.ANYWHERE));
		}

		return userService.findByPage(queryCondition, pageIndex, pageSize);
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.GET)
	@SerializationFilters(filters = { @SerializationFilter(target = User.class, fields = { "password" }) })
	public Object get(@PathVariable String userId) {
		return Result.successResult(userService.get(userId), null);
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Object save(@RequestBody User user) {
		user = userService.addUser(user, user.getSite().getId());
		return Result.successResult(user.getId(), "新增成功");
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
	public Object update(@PathVariable String userId, @RequestBody User user) {
		user.setId(userId);
		userService.update(user);
		return Result.successResult("更新成功");
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
	public Object delete(@PathVariable String userId) {
		userService.delete(userId);
		return Result.successResult("删除成功");
	}

}
