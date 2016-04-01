package com.demonshrimp.dawj.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.SiteDao;
import com.demonshrimp.dawj.model.dao.UserDao;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.model.entity.Site.Status;
import com.demonshrimp.dawj.service.SystemService;

import pers.ksy.common.StringUtil;
import pers.ksy.common.model.Page;
import pers.ksy.common.orm.Conditions;
import pers.ksy.common.orm.QueryCondition;

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
	public Site addSite(Site site) {
		site.setCreateTime(new Date());
		site.setStatus(Status.ENABLED);
		siteDao.save(site);
		return site;
	}

	@Override
	public Site update(Site site) throws ServiceException {
		List<String> fieldNames = new ArrayList<>();
		if (StringUtil.isEmpty(site.getPassword())) {
			fieldNames.add("password");
		}
		Site s = update(site, false, fieldNames.toArray(new String[] {}));
		return s;
	}

	@Override
	public void setSiteStatus(String siteId, Site.Status status) {
		Site site = siteDao.get(siteId);
		if (null == site) {
			throw new ServiceException("站点不存在");
		}
		site.setStatus(status);
		siteDao.update(site);
	}

	@Override
	public Page<Site> findByPage(QueryCondition qc, int pageIndex, int pageSize) {
		qc.add(Conditions.ne("id", "root_site"));
		return super.findByPage(qc, pageIndex, pageSize);
	}

	@Override
	public Site siteAdminLogin(String name, String password) throws ServiceException {
		Site site = siteDao.getByProperty("name", name);
		if (null == site) {
			throw new ServiceException("站点不存在");
		}
		if (!site.getPassword().equals(password)) {
			throw new ServiceException("密码错误");
		}
		if (site.getStatus() == Status.DISABLED) {
			throw new ServiceException("站点已停用");
		}
		String token = UUID.randomUUID().toString().replaceAll("-", "");
		site.setToken(token);
		site.setLastLoginTime(new Date());
		siteDao.update(site);
		return site;
	}

	@Override
	public Site getCurrentLoginSite(String token) {
		Site site = siteDao.getByProperty("token", token);
		if (null == site) {
			throw new ServiceException("令牌无效", "900");
		}
		long diffTime = System.currentTimeMillis() - site.getLastLoginTime().getTime();
		if (diffTime > 1000 * 60 * 60 * 1) {
			throw new ServiceException("令牌无效", "901");
		}
		return site;
	}

	@Override
	public Site getSiteByPath(String path) {
		Site site = siteDao.getByProperty("path", path);
		Assert.notNull(site,"站点不存在");
		return site;
	}

}
