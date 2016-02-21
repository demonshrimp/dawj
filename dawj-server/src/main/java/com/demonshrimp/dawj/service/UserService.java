package com.demonshrimp.dawj.service;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.entity.Site;
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
	 * @param mobile
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	User login(String mobile, String password) throws ServiceException;


	/**
	 * 根据用户手机查询用户
	 * @param mobile
	 * @return
	 */
	public User getUserByMobile(String mobile);


}
