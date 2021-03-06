package com.demonshrimp.dawj.web.controller.user;

import javax.servlet.http.HttpServletRequest;
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
		user = userService.addUser(user, getCurrentSite().getId());
		return Result.successResult(user.getId(), "注册成功");
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
		return Result.successResult("验证码发送成功");
	}

	@RequestMapping(path = "/send-email-captcha", method = RequestMethod.GET)
	public Object sendEmailCaptcha(String email) {
		userService.sendCaptcha(email, MessageService.Type.EMAIL);
		return Result.successResult("验证码发送成功");
	}

	@RequestMapping(path = "/check-captcha", method = RequestMethod.GET)
	public Object checkCaptcha(String contactInfo, int captcha) {
		if (userService.checkCaptcha(contactInfo, captcha)) {
			return Result.successResult();
		}
		return Result.errorResult("验证码错误");
	}

	@RequestMapping(path = "/personal-details", method = RequestMethod.GET)
	public Object personalDetails() {
		return Result.successResult(getCurrentUser(), null);
	}

	@RequestMapping(path = "/qq-login", method = { RequestMethod.GET, RequestMethod.POST })
	public Object qqLoginCheck(HttpServletRequest request, HttpServletResponse response, String accessToken) {
		User user = userService.qqLogin(accessToken);
		return Result.successResult(user, null);
	}

	@RequestMapping(path = "/wechat-login", method = { RequestMethod.GET, RequestMethod.POST })
	public Object loginFromWechat(String code) {
		User user = userService.loginFromWechat(code, getCurrentSite().getId());
		return Result.successResult(user, null);
	}

	@RequestMapping(path = "/wechat-share-success", method = RequestMethod.GET)
	public Object wechatShareSuccess() {
		int points = userService.addSharePoints(getCurrentUser().getId());
		return Result.successResult(points, "分享成功，获得" + points + "元奖励");
	}
	
	@RequestMapping(path = "/psychological-test", method = RequestMethod.GET)
	public Object psychologicalTest(int points){
		userService.psychologicalTest(getCurrentUser().getId(), points);
		return Result.successResult();
	}
	
	@RequestMapping(path = "/password-reset", method = RequestMethod.POST)
	public Object passwordReset(String mobile, String password, int captcha) {
		userService.passwordReset(mobile, password, captcha);
		return Result.successResult("密码重置成功!");
	}
}
