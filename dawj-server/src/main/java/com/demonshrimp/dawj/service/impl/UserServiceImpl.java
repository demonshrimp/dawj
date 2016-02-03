package com.demonshrimp.dawj.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.UserDao;
import com.demonshrimp.dawj.model.entity.User;
import com.demonshrimp.dawj.model.entity.UserIntegralAccount;
import com.demonshrimp.dawj.service.UserService;

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
		UserIntegralAccount integralAccount = new UserIntegralAccount();
		integralAccount.setPoints((long) 0);
		integralAccount.setCreateTime(new Date());
		integralAccount.setUser(user);
		user.setIntegralAccount(integralAccount);
		return user;
	}

	@Override
	public User update(User user) throws ServiceException {
		User u = update(user, false, "integralAccount", "mobile", "password", "lastLoginTime");
		return u;
	}

	@Override
	public User delete(String id) {
		User user = userDao.get(id);
		// user.setDeleted(true);
		userDao.update(user);
		return user;
	}

	@Override
	public User login(String mobile, String password) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByMobile(String mobile) {
		return userDao.getByProperty("mobile", mobile);
	}

}
