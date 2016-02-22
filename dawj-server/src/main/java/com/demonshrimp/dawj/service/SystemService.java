package com.demonshrimp.dawj.service;

import com.demonshrimp.dawj.model.entity.Site;

public interface SystemService extends BaseService<Site,String>{
	

	/**
	 * 添加站点
	 * @param site
	 * @return
	 */
	public Site addSite(Site site);

	
}
