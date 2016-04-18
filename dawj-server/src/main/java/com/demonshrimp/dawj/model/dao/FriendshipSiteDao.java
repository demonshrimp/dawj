package com.demonshrimp.dawj.model.dao;

import java.util.List;

import com.demonshrimp.dawj.model.entity.FriendshipSite;

public interface FriendshipSiteDao extends BaseDao<FriendshipSite, String> {

	int deleteNotIn(List<FriendshipSite> friendshipSites);
}