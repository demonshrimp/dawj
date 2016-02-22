package com.demonshrimp.dawj.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.SiteDao;
import com.demonshrimp.dawj.model.dao.UserDao;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.SystemService;

@Transactional
@Service
public class SystemServiceImpl extends BaseServiceImpl<Site, String> implements SystemService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private SiteDao siteDao;

	@Override
	public BaseDao<Site, String> getDao() {
		return siteDao;
	}

	@Override
	public Site addSite(Site site){
		site.setCreateTime(new Date());
		siteDao.save(site);
		return site;
	}
	
}
