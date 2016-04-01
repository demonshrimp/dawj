package com.demonshrimp.dawj.web.controller.user;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.User;
import com.demonshrimp.dawj.service.MessageService;
import com.demonshrimp.dawj.service.UserService;

import pers.ksy.common.model.Result;

@RestController(value = "userUserController")
@RequestMapping(value = "/usr/user")
public class UserController extends BaseUserController {
	@Autowired
	private UserService userService;

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public Object login(String anyAccount, String password, HttpServletResponse response) {
		User user = userService.login(anyAccount, password);
		return Result.successResult(user, "");
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Object save(@RequestBody User user) {
		user = userService.addUser(user, user.getSite().getId());
		return Result.successResult(user.getId(), "新增成功");
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
	public Object update(@PathVariable String userId, @RequestBody User user) {
		user.setId(userId);
		userService.update(user);
		return Result.successResult("更新成功");
	}

	@RequestMapping(path = "/send-mobile-captcha", method = RequestMethod.GET)
	public Object sendMobileCaptcha(String mobile) {
		userService.sendCaptcha(mobile, MessageService.Type.MOBILE);
		return Result.successResult();
	}

	@RequestMapping(path = "/send-email-captcha", method = RequestMethod.GET)
	public Object sendEmailCaptcha(String email) {
		userService.sendCaptcha(email, MessageService.Type.EMAIL);
		return Result.successResult();
	}

	@RequestMapping(path = "/check-captcha", method = RequestMethod.GET)
	public Object checkCaptcha(String contactInfo, int captcha) {
		if(userService.checkCaptcha(contactInfo, captcha)){
			return Result.successResult();
		}
		return Result.errorResult("验证码错误");
	}

}
