package com.demonshrimp.dawj.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.model.entity.User;
import com.demonshrimp.dawj.service.SystemService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;
import pers.ksy.common.orm.Conditions;
import pers.ksy.common.orm.MatchMode;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.QueryConditionImpl;

@RestController
@RequestMapping(value = "/site")
public class SiteController extends BaseController {
	@Autowired
	private SystemService systemService;

	@RequestMapping(path = "/page", method = RequestMethod.GET)
	@SerializationFilter(target = Site.class, fields = { "password" })
	public Object page(String city, String name, int pageIndex, int pageSize) {
		QueryCondition queryCondition = new QueryConditionImpl(User.class, null);
		if (null != city) {
			queryCondition.add(Conditions.like("city", city, MatchMode.ANYWHERE));
		}
		if (null != name) {
			queryCondition.add(Conditions.like("name", name, MatchMode.ANYWHERE));
		}

		return systemService.findByPage(queryCondition, pageIndex, pageSize);
	}

	@RequestMapping(path = "/{siteId}", method = RequestMethod.GET)
	@SerializationFilter(target = Site.class, fields = { "password" })
	public Object get(@PathVariable String siteId) {
		return Result.successResult(systemService.get(siteId), null);
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Object save(@RequestBody Site site) {
		site = systemService.addSite(site);
		return Result.successResult(site.getId(), "新增成功");
	}

	@RequestMapping(path = "/{siteId}", method = RequestMethod.PUT)
	public Object update(@PathVariable String siteId, @RequestBody Site site) {
		site.setId(siteId);
		systemService.update(site);
		return Result.successResult("更新成功");
	}

}
