package com.demonshrimp.dawj.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.SystemService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.QueryConditionImpl;

@RestController(value = "siteUserController")
@RequestMapping(value = "/usr/site")
public class SiteController extends BaseUserController {
	@Autowired
	private SystemService systemService;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	@SerializationFilter(target = Site.class, fields = { "id", "createTime", "lastModifyTime", "certificated", "status",
			"password", "token", "lastLoginTime" })
	public Object list() {
		QueryCondition queryCondition = new QueryConditionImpl(Site.class, null);
		return Result.successResult(systemService.findList(queryCondition), null);
	}

}
