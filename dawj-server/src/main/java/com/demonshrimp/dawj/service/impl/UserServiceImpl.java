package com.demonshrimp.dawj.service.impl;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.message.sms.weimi.WeiMiSmsProvider;
import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.SiteDao;
import com.demonshrimp.dawj.model.dao.UserDao;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.model.entity.User;
import com.demonshrimp.dawj.service.CaptchaService;
import com.demonshrimp.dawj.service.MessageService;
import com.demonshrimp.dawj.service.MessageService.Type;
import com.demonshrimp.dawj.service.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pers.ksy.common.orm.QueryCondition;

@Transactional
@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {
	private String TMPL_CAPTCHA = "【大爱无疆】尊敬的用户，你的手机验证码是：%P%，30分钟内有效。请不要把此验证码泄露给任何人，以便你能安全使用。";

	@Autowired
	private UserDao userDao;
	@Autowired
	private SiteDao siteDao;
	@Autowired
	private MessageService messageService;
	@Autowired
	private CaptchaService captchaService;
	@Autowired
	private WeiMiSmsProvider weiMiSmsProvider;

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
		User u = update(user, false, "integralAccount", "mobile", "password", "lastLoginTime", "qqOpenId",
				"wechatUserId");
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
		qc.eq("mobile", anyAccount);
		User user = userDao.uniqueByQC(qc);
		if (null == user) {
			throw new ServiceException("用户不存在");
		}
		if (!user.getPassword().equals(password)) {
			throw new ServiceException("密码错误");
		}
		user = loginHandle(user);
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
		if (type == Type.MOBILE) {
			System.out.println(contactInfo + ":" + captcha);
			String json = weiMiSmsProvider.sendCaptcha(contactInfo, captcha);
			JsonObject obj = new Gson().fromJson(json, JsonObject.class);
			System.out.println(json);
			if (obj.get("code").getAsInt() != 0) {
				throw new ServiceException(obj.get("msg").getAsString());
			}
		} else {
			messageService.sendMessage(contactInfo, "【大爱无疆】验证码", buildCaptchaContent(captcha), type);
		}
		captchaService.storeCaptcha(contactInfo, captcha);
	}

	@Override
	public boolean checkCaptcha(String contactInfo, int captcha) {
		return captchaService.checkCaptcha(contactInfo, captcha, true);
	}

	@Override
	public User qqLogin(String accessToken) {
		User user = null;
		HttpClient httpClient = new HttpClient();
		HttpMethod method = new GetMethod("https://graph.qq.com/oauth2.0/me?access_token=" + accessToken);
		try {
			httpClient.executeMethod(method);
			String json = method.getResponseBodyAsString();
			json = json.replace("callback(", "").replace(");", "");
			JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
			String openid = jsonObject.get("openid").getAsString();
			user = userDao.getByProperty("qqOpenId", openid);
			if (null != user) {
				user = loginHandle(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return user;
	}

	private User loginHandle(User user) {
		String token = UUID.randomUUID().toString().replaceAll("-", "");
		user.setToken(token);
		user.setLastLoginTime(new Date());
		userDao.update(user);
		return user;
	}

	private String buildCaptchaContent(int captcha) {
		return TMPL_CAPTCHA.replace("%P%", captcha + "");
	}
}
