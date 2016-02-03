package com.demonshrimp.dawj.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.registry.infomodel.Organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.UserDao;
import com.demonshrimp.dawj.model.entity.User;
import com.demonshrimp.dawj.service.UserService;

import pers.ksy.common.MD5Util;
import pers.ksy.common.orm.Conditions;
import pers.ksy.common.orm.Order;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.QueryConditionImpl;
import pers.ksy.common.orm.jpa.JpaHelper;

@Transactional
@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

	@Autowired
	UserDao userDao;

	@Override
	public BaseDao<User, String> getDao() {
		return userDao;
	}

	@Override
	public User addUser(User user) throws ServiceException {
		save(user);
		return user;
	}

	@Override
	public User update(User user) throws ServiceException {
		User u = update(user, false, "roles", "username", "password", "lastLoginTime");
		return u;
	}

	@Override
	public User delete(String id){
		User user = userDao.get(id);
		//user.setDeleted(true);
		userDao.update(user);
		return user;
	}

	@Override
	public User login(String mobile, String password) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}


	

}
