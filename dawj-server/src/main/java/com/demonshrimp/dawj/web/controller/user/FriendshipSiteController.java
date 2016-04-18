package com.demonshrimp.dawj.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.service.SystemService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;

@RestController(value = "friendshipSiteUserController")
@RequestMapping(value = "/usr/friendship-site")
public class FriendshipSiteController extends BaseUserController {

	@Autowired
	private SystemService systemService;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	@SerializationFilter
	public Object list() {
		return Result.successResult(systemService.friendshipSiteList(), null);
	}

}
