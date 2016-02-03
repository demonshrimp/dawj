package com.demonshrimp.dawj.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.xml.registry.infomodel.Organization;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.demonshrimp.dawj.model.entity.User;
import com.demonshrimp.dawj.service.UserService;
import com.demonshrimp.dawj.test.BaseTestCase;

public class UserServiceTest extends BaseTestCase{	
	@Autowired
	UserService service;
	
	Organization root;
	
	private String testMobile = "13988888888";
	
	
	@Before
	public void setup(){
		service.deleteAll();
		create3Users();
	}
	
	private void create3Users(){
		for(int i=0;i<3;i++){
			User user = new User();
			user.setMobile("138888888"+i);
			user.setName("用户_"+i);
			user.setPassword("123456");
			user.setSex(User.Sex.FEMALE);
			user.setCreateTime(new Date());
			user.setLastModifyTime(new Date());
			service.addUser(user);
		}
	}
	
	//唯一约束测试
	//@Test
	public void testUKCons(){
		User user = new User();
		user.setMobile("1388888880");
		user.setName("用户_4");
		user.setPassword("123456");
		user.setSex(User.Sex.FEMALE);
		user.setCreateTime(new Date());
		user.setLastModifyTime(new Date());
		Exception ex = null;
		try{
			service.addUser(user);
		}catch(Exception e){
			ex = e;
		}
		assertNotNull(ex);
	}

	@Test
	public void testAddUser() {
		User user = new User();
		user.setName("name");
		user.setMobile(testMobile);
		user.setSex(User.Sex.FEMALE);
		user.setCreateTime(new Date());
		user.setLastModifyTime(new Date());
		user.setPassword("123456"); 
		service.addUser(user);
		
		User loaded = service.get(user.getId());
		assertNotNull(loaded);
		assertEquals("name",loaded.getName());
	}
	
	@Test
	public void testUpdateUser(){
		User user1 = service.getUserByMobile("1388888880");
		user1.setName("用户100");
		service.update(user1);
		user1 = service.get(user1.getId());
		assertEquals("用户100",user1.getName());
	}
	
	@Test
	public void testDeleteUser(){
		User user1 = service.getUserByMobile("1388888880");
		service.delete(user1);
		service.get(user1.getId());
	}
	

}
