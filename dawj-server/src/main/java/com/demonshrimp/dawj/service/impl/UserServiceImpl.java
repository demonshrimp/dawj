package com.demonshrimp.dawj.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.SiteDao;
import com.demonshrimp.dawj.model.dao.UserDao;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.model.entity.User;
import com.demonshrimp.dawj.service.CaptchaService;
import com.demonshrimp.dawj.service.MessageService;
import com.demonshrimp.dawj.service.UserService;

import pers.ksy.common.orm.QueryCondition;

@Transactional
@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private SiteDao siteDao;
	@Autowired
	private MessageService messageService;
	@Autowired
	private CaptchaService captchaService;

	@Override
	public BaseDao<User, String> getDao() {
		return userDao;
	}

	@Override
	public User addUser(User user, String siteId) throws ServiceException {
		Site site = siteDao.get(siteId);
		user.setSite(site);
		user.setPoints(0);
		save(user);
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
	public User login(String anyAccount, String password) throws ServiceException {
		QueryCondition qc = userDao.getQC();
		User user = userDao.uniqueByQC(qc);
		if (null == user) {
			throw new ServiceException("站点不存在");
		}
		if (!user.getPassword().equals(password)) {
			throw new ServiceException("密码错误");
		}
		String token = UUID.randomUUID().toString().replaceAll("-", "");
		user.setToken(token);
		user.setLastLoginTime(new Date());
		userDao.update(user);
		return user;
	}

	@Override
	public User getUserByMobile(String mobile) {
		return userDao.getByProperty("mobile", mobile);
	}

	@Override
	public User getCurrentLoginUser(String token) {
		User user = userDao.getByProperty("token", token);
		if (null == user) {
			throw new ServiceException("令牌无效", "900");
		}
		long diffTime = System.currentTimeMillis() - user.getLastLoginTime().getTime();
		if (diffTime > 1000 * 60 * 60 * 1) {
			throw new ServiceException("令牌无效", "901");
		}
		return user;
	}

	@Override
	public void sendCaptcha(String contactInfo, MessageService.Type type) {
		int captcha = captchaService.generateCaptcha();
		messageService.sendMessage(contactInfo, "【大爱无疆】验证码", "欢迎来到大爱无疆，您的验证码为：" + captcha, type);
		captchaService.storeCaptcha(contactInfo, captcha);
	}

	@Override
	public boolean checkCaptcha(String contactInfo, int captcha) {
		return captchaService.checkCaptcha(contactInfo, captcha, true);
	}

}
