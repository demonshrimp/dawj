package com.demonshrimp.dawj.web.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.SystemService;
import com.demonshrimp.dawj.web.controller.BaseController;

public abstract class BaseAdminController extends BaseController {

	@Autowired
	private SystemService systemService;

	public static final String KEY_TOKEN = "_stoken";

	private static ThreadLocal<Site> siteHolder = new ThreadLocal<Site>();

	public Site getCurrentSite() {
		Site site = siteHolder.get();
		if (site == null) {
			String token = getToken();
			site = systemService.getCurrentLoginSite(token);
		}
		return site;
	}

	protected String getToken() {
		HttpServletRequest request = getRequest();
		return request.getHeader(KEY_TOKEN);
	}

	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

}
