package com.demonshrimp.dawj.web.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.model.entity.User;
import com.demonshrimp.dawj.service.SystemService;
import com.demonshrimp.dawj.service.UserService;
import com.demonshrimp.dawj.web.controller.BaseController;

import pers.ksy.common.StringUtil;

public abstract class BaseUserController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private SystemService systemService;

	public static final String KEY_TOKEN = "_utoken";

	public static final String KEY_SITE_PATH = "_spath";

	private static ThreadLocal<User> userHolder = new ThreadLocal<User>();
	private static ThreadLocal<Site> siteHolder = new ThreadLocal<Site>();

	public User getCurrentUser() {
		User user = userHolder.get();
		if (user == null) {
			String token = getToken();
			user = userService.getCurrentLoginUser(token);
		}
		return user;
	}

	public Site getCurrentSite() {
		Site site = siteHolder.get();
		if (site == null) {
			String path = getSitePath();
			site = systemService.getSiteByPath(path);
		}
		return site;
	}

	protected String getToken() {
		HttpServletRequest request = getRequest();
		return request.getHeader(KEY_TOKEN);
	}
	
	protected String getSitePath() {
		HttpServletRequest request = getRequest();
		String path = request.getHeader(KEY_SITE_PATH);
		if(StringUtil.isEmpty(path)){
			path = "/";
		}
		return path;
	}

	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

}
