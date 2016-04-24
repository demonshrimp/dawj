package com.demonshrimp.dawj.service;

import java.util.List;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.entity.FriendshipSite;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.model.entity.Site.Status;

public interface SystemService extends BaseService<Site,String>{
	

	/**
	 * 添加站点
	 * @param site
	 * @return
	 */
	public Site addSite(Site site);

	/**
	 * 站点管理登录
	 * @param name 站点名
	 * @param password 管理密码
	 * @return
	 * @throws ServiceException
	 */
	Site siteAdminLogin(String name, String password) throws ServiceException;

	/**
	 * 设置站点状态
	 * @param siteId 站点ID
	 * @param status 站点状态
	 */
	void setSiteStatus(String siteId, Status status);

	/**
	 * 根据令牌获取当前登录用户
	 * @param token
	 * @return
	 */
	Site getCurrentLoginSite(String token);

	/**
	 * 
	 * @param path
	 * @return
	 */
	public Site getSiteByPath(String path);

	/**
	 * 添加友情站点
	 * @param friendshipSite
	 * @return
	 */
	FriendshipSite addFriendshipSite(FriendshipSite friendshipSite);

	/**
	 * 更新友情站点
	 * @param friendshipSite
	 * @return
	 */
	FriendshipSite updateFriendshipSite(FriendshipSite friendshipSite);

	/**
	 * 获取友情站点
	 * @param friendshipSiteId
	 * @return
	 */
	FriendshipSite getFriendshipSite(String friendshipSiteId);
	
	/**
	 * 获取友情站点
	 * @param friendshipSiteId
	 * @return
	 */
	void delFriendshipSite(String friendshipSiteId);
	
	/**
	 * 友情站点列表
	 * @return
	 */
	List<FriendshipSite> friendshipSiteList();

	/**
	 * 批量存储友情站点
	 * @param friendshipSites
	 */
	public void batchSaveFriendshipSites(List<FriendshipSite> friendshipSites);

	public Site getSiteByName(String name);

	
}
