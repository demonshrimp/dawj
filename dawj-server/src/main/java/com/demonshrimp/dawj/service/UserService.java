package com.demonshrimp.dawj.service;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.entity.User;

public interface UserService extends BaseService<User,String>{
	/**
	 * 在某站点下新增一个用户
	 * @param user
	 * @param orgId
	 * @return
	 */
	public User addUser(User user, String siteId);
	
	
	/**
	 * 更新用户
	 */
	public User update(User user);
	
	/**
	 * 逻辑删除用户
	 * @param user
	 * @return
	 */
	public User delete(String id);
	

	/**
	 * 用户登录
	 * @param anyAccount 任意账号 mobile/email/wechatUserId/qq
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	User login(String anyAccount, String password) throws ServiceException;


	/**
	 * 根据用户手机查询用户
	 * @param mobile
	 * @return
	 */
	public User getUserByMobile(String mobile);


	public User getCurrentLoginUser(String token);
	
	/**
	 * 发送验证码
	 * @param contactInfo
	 */
	public void sendCaptcha(String contactInfo, MessageService.Type type);
	
	/**
	 * 校验验证码
	 * @param contactInfo
	 * @param captcha
	 */
	public boolean checkCaptcha(String contactInfo, int captcha);


	/**
	 * QQ登录
	 * @param accessToken
	 * @return
	 */
	User qqLogin(String accessToken);


	/**
	 * 微信登录(如果没用户则注册)
	 * @param code
	 * @return
	 */
	public User loginFromWechat(String code, String siteId);


	/**
	 * 添加分享积分
	 * @param userId
	 * @return
	 */
	public int addSharePoints(String userId);


	/**
	 * 使用积分进行测评
	 * @param userId
	 * @param points 
	 */
	public void psychologicalTest(String userId, int points);


	/**
	 * 密码重置
	 * @param mobile
	 * @param captcha
	 */
	public String passwordReset(String mobile, int captcha);


}
