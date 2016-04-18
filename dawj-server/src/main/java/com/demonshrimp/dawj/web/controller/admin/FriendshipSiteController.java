package com.demonshrimp.dawj.web.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.FriendshipSite;
import com.demonshrimp.dawj.service.SystemService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;

@RestController(value = "friendshipSiteAdminController")
@RequestMapping(value = "/admin/friendship-site")
public class FriendshipSiteController extends BaseAdminController {

	@Autowired
	private SystemService systemService;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	@SerializationFilter
	public Object list() {
		return Result.successResult(systemService.friendshipSiteList(), null);
	}

	@RequestMapping(path = "/batch-save", method = RequestMethod.POST)
	@SerializationFilter
	public Object batchSave(@RequestBody List<FriendshipSite> friendshipSites) {
		systemService.batchSaveFriendshipSites(friendshipSites);
		return Result.successResult("保存成功");
	}

}
