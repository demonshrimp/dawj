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

import pers.ksy.common.MD5Util;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.wechat.WechatService;
import pers.ksy.common.wechat.api.model.AccessToken;
import pers.ksy.common.wechat.api.model.WechartUserInfo;

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
	@Autowired
	private WechatService wechatService;

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
		User u = update(user, "name", "qq", "sex", "password", "birthday");
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
		if (diffTime > 1000 * 60 * 60 * 3) {
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

	@Override
	public User loginFromWechat(String code, String siteId) {
		AccessToken accessToken = wechatService.getAccessToken(code);
		if (accessToken.isInvalid()) {
			throw new ServiceException(accessToken.getErrmsg());
		}
		User user = userDao.getByProperty("wechatUserId", accessToken.getOpenid());
		if (null == user) {
			WechartUserInfo userInfo = wechatService.getUserInfo(accessToken);
			if (userInfo.isInvalid()) {
				throw new ServiceException(userInfo.getErrmsg());
			}
			user = new User();
			user.setWechatUserId(userInfo.getOpenid());
			user.setName(userInfo.getNickname());
			if (null != userInfo.getSex()) {
				if ("1".equals(userInfo.getSex())) {
					user.setSex(User.Sex.MALE);
				}
				if ("2".equals(userInfo.getSex())) {
					user.setSex(User.Sex.FEMALE);
				}
				user = addUser(user, siteId);
				user.setPoints(5);
				userDao.update(user);
			}
		}
		user = loginHandle(user);
		return user;
	}

	@Override
	public int addSharePoints(String userId) {
		int points = (int) (Math.random() * 59) + 1;
		User user = userDao.load(userId);
		user.setPoints(user.getPoints() + points);
		userDao.update(user);
		return points;
	}

	@Override
	public void psychologicalTest(String userId,int points) {
		User user = userDao.load(userId);
		if (user.getPoints() < points) {
			throw new ServiceException("当前积分不足，无法进行评测！");
		}
		user.setPoints(user.getPoints() - points);
		userDao.update(user);
	}
	
	

	@Override
	public void passwordReset(String mobile, String password, int captcha) {
		if(!checkCaptcha(mobile, captcha)){
			throw new ServiceException("验证码错误！");
		}
		QueryCondition qc = userDao.getQC();
		qc.eq("mobile", mobile);
		User user = userDao.uniqueByQC(qc);
		if (null == user) {
			throw new ServiceException("用户不存在");
		}
		user.setPassword(password);
		userDao.update(user);
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
