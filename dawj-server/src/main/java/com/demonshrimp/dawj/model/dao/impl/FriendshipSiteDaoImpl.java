package com.demonshrimp.dawj.model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.demonshrimp.dawj.model.dao.FriendshipSiteDao;
import com.demonshrimp.dawj.model.entity.FriendshipSite;

import pers.ksy.common.StringUtil;

@Repository
public class FriendshipSiteDaoImpl extends BaseDaoImpl<FriendshipSite, String> implements FriendshipSiteDao {

	@Override
	public int deleteNotIn(List<FriendshipSite> friendshipSites) {
		String hql = "delete From FriendshipSite where id not in :ids";
		List<String> ids = new ArrayList<>();
		for (FriendshipSite friendshipSite : friendshipSites) {
			if (StringUtil.notEmpty(friendshipSite.getId())) {
				ids.add(friendshipSite.getId());
			}
		}
		if (ids.size() > 0) {
			Query query = createQuery(hql);
			query.setParameterList("ids", ids);
			return query.executeUpdate();
		}
		return 0;
	}

}