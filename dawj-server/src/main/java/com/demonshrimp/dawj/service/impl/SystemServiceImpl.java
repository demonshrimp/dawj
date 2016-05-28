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
import com.demonshrimp.dawj.model.dao.FriendshipSiteDao;
import com.demonshrimp.dawj.model.dao.SiteDao;
import com.demonshrimp.dawj.model.dao.SystemConfigDao;
import com.demonshrimp.dawj.model.dao.UserDao;
import com.demonshrimp.dawj.model.entity.FriendshipSite;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.model.entity.Site.Status;
import com.demonshrimp.dawj.service.SystemService;

import pers.ksy.common.MD5Util;
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
	@Autowired
	private SystemConfigDao systemConfigDao;
	@Autowired
	private FriendshipSiteDao friendshipSiteDao;

	private static final String LICENSE = "ksy8888!";
	
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
		if (diffTime > 1000 * 60 * 60 * 3) {
			throw new ServiceException("令牌无效", "901");
		}
		return site;
	}

	@Override
	public Site getSiteByPath(String path) {
		Site site = siteDao.getByProperty("path", path);
		Assert.notNull(site, "站点不存在");
		return site;
	}

	@Override
	public FriendshipSite addFriendshipSite(FriendshipSite friendshipSite) {
		friendshipSite.setCreateTime(new Date());
		friendshipSiteDao.save(friendshipSite);
		return friendshipSite;
	}

	@Override
	public FriendshipSite updateFriendshipSite(FriendshipSite friendshipSite) {
		friendshipSiteDao.update(friendshipSite, "name", "url", "description", "lastModifyTime");
		return friendshipSite;
	}

	@Override
	public FriendshipSite getFriendshipSite(String friendshipSiteId) {
		return friendshipSiteDao.load(friendshipSiteId);
	}

	@Override
	public void delFriendshipSite(String friendshipSiteId) {
		friendshipSiteDao.deleteById(friendshipSiteId);
	}

	@Override
	public List<FriendshipSite> friendshipSiteList() {
		return friendshipSiteDao.findAll();
	}

	@Override
	public void batchSaveFriendshipSites(List<FriendshipSite> friendshipSites) {
		int length = friendshipSiteDao.deleteNotIn(friendshipSites);
		for (FriendshipSite friendshipSite : friendshipSites) {
			if (StringUtil.isEmpty(friendshipSite.getId())) {
				addFriendshipSite(friendshipSite);
			} else {
				updateFriendshipSite(friendshipSite);
			}
		}
	}

	@Override
	public Site getSiteByName(String name) {
		Site site = siteDao.getByProperty("name", name);
		if(site==null){
			throw new ServiceException("站点不存在!");
		}
		return site;
	}
	
	@Override
	public boolean checkLicense(){
		String license = systemConfigDao.getLicense();
		return license!=null&&MD5Util.MD5(LICENSE).equals(license);
	}
	
	@Override
	public void setLicense(String license){
		systemConfigDao.setLicense(MD5Util.MD5(license));
	}
	
}
