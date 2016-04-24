package com.demonshrimp.dawj.web.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.SystemService;
import com.demonshrimp.dawj.web.controller.BaseController;

public abstract class BaseAdminController extends BaseController {

	@Autowired
	private SystemService systemService;

	public static final String KEY_TOKEN = "_stoken";
	public static final String KEY_SITE_NAME = "_sname";

	private static ThreadLocal<Site> siteHolder = new ThreadLocal<Site>();

	public Site getCurrentSite() {
		Site site = siteHolder.get();
		if (site == null) {
			String name = getSiteName();
			String token = getToken();
			site = systemService.getCurrentLoginSite(token);
			if (name != null && !name.equals(site.getName())) {
				if (site.getId().equals("root_site")) {
					site = systemService.getSiteByName(name);
				} else {
					throw new ServiceException("当前登录用户与站点不一致，请重新登录", "901");
				}
			}
		}
		return site;
	}

	protected String getToken() {
		HttpServletRequest request = getRequest();
		return request.getHeader(KEY_TOKEN);
	}

	protected String getSiteName() {
		HttpServletRequest request = getRequest();
		return request.getHeader(KEY_SITE_NAME);
	}

	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

}
