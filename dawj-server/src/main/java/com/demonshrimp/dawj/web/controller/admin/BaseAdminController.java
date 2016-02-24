package com.demonshrimp.dawj.web.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.web.controller.BaseController;

public abstract class BaseAdminController extends BaseController{
	@Autowired
    protected  HttpServletRequest request;
	
	public static final String KEY_CURRENT_USER = "current-admin";
	

	public Site getCurrentSite() {
		return (Site) request.getSession().getAttribute(KEY_CURRENT_USER);
	}

	public void setCurrentSite(Site site) {
		this.request.getSession().setAttribute(KEY_CURRENT_USER, site);
	}
	
	
}
