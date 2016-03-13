package com.demonshrimp.dawj.web.controller.admin;

import javax.servlet.http.Cookie;
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
	
	public static final String KEY_TOKEN_HEADER = "_stoken";
	
	private static ThreadLocal<Site> siteHolder = new ThreadLocal<Site>();
	private static ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

	public Site getCurrentSite() {
		Site site = siteHolder.get();
		if (site == null) {
			String token = getToken();
			site = systemService.getCurrentLoginSite(token);
		}
		return site;
	}
	
	protected String getToken() {
		for (Cookie cookie : getRequest().getCookies()) {
			if (cookie.getName() == KEY_TOKEN_HEADER) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public HttpServletRequest getRequest() {
		HttpServletRequest request = requestHolder.get();
		if (request == null) {
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			requestHolder.set(request);
		}
		return request;
	}

}
