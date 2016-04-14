package com.demonshrimp.dawj.web.controller.user;

import java.io.IOException;
import java.util.ArrayList;

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
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.PageFans;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.PageFansBean;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.javabeans.weibo.Company;
import com.qq.connect.oauth.Oauth;

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
	public void qqLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		try {
			response.sendRedirect(new Oauth().getAuthorizeURL(request));
		} catch (QQConnectException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(path = "/qq-login-callback", method = { RequestMethod.GET, RequestMethod.POST })
	public Object qqLoginCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Result result = Result.errorResult("QQ登录失败");
		try {
			AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);

			String accessToken = null, openID = null;
			long tokenExpireIn = 0L;

			if (accessTokenObj.getAccessToken().equals("")) {
				// 我们的网站被CSRF攻击了或者用户取消了授权
				// 做一些数据统计工作

				System.out.print("没有获取到响应参数");
				result.setMessage("没有获取到响应参数");
			} else {
				accessToken = accessTokenObj.getAccessToken();
				tokenExpireIn = accessTokenObj.getExpireIn();

				// 利用获取到的accessToken 去获取当前用的openid -------- start
				OpenID openIDObj = new OpenID(accessToken);
				openID = openIDObj.getUserOpenID();
				UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
				UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
				if (userInfoBean.getRet() == 0) {
					result.success(userInfoBean, null);
					System.out.println(userInfoBean.getNickname() + "<br/>");
					System.out.println(userInfoBean.getGender() + "<br/>");
					System.out.println("黄钻等级： " + userInfoBean.getLevel() + "<br/>");
					System.out.println("会员 : " + userInfoBean.isVip() + "<br/>");
					System.out.println("黄钻会员： " + userInfoBean.isYellowYearVip() + "<br/>");
					System.out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL30() + "/><br/>");
					System.out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL50() + "/><br/>");
					System.out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL100() + "/><br/>");
				} else {
					System.out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + userInfoBean.getMsg());
				}

				PageFans pageFansObj = new PageFans(accessToken, openID);
				PageFansBean pageFansBean = pageFansObj.checkPageFans("97700000");
				if (pageFansBean.getRet() == 0) {
					System.out.println("<p>验证您" + (pageFansBean.isFans() ? "是" : "不是") + "QQ空间97700000官方认证空间的粉丝</p>");
				} else {
					System.out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + pageFansBean.getMsg());
				}

				com.qq.connect.api.weibo.UserInfo weiboUserInfo = new com.qq.connect.api.weibo.UserInfo(accessToken,
						openID);
				com.qq.connect.javabeans.weibo.UserInfoBean weiboUserInfoBean = weiboUserInfo.getUserInfo();
				if (weiboUserInfoBean.getRet() == 0) {
					// 获取用户的微博头像----------------------start
					System.out.println("<image src=" + weiboUserInfoBean.getAvatar().getAvatarURL30() + "/><br/>");
					System.out.println("<image src=" + weiboUserInfoBean.getAvatar().getAvatarURL50() + "/><br/>");
					System.out.println("<image src=" + weiboUserInfoBean.getAvatar().getAvatarURL100() + "/><br/>");
					// 获取用户的微博头像 ---------------------end

					// 获取用户的生日信息 --------------------start
					System.out.println("<p>尊敬的用户，你的生日是： " + weiboUserInfoBean.getBirthday().getYear() + "年"
							+ weiboUserInfoBean.getBirthday().getMonth() + "月"
							+ weiboUserInfoBean.getBirthday().getDay() + "日");
					// 获取用户的生日信息 --------------------end

					StringBuffer sb = new StringBuffer();
					sb.append("<p>所在地:" + weiboUserInfoBean.getCountryCode() + "-" + weiboUserInfoBean.getProvinceCode()
							+ "-" + weiboUserInfoBean.getCityCode() + weiboUserInfoBean.getLocation());

					// 获取用户的公司信息---------------------------start
					ArrayList<Company> companies = weiboUserInfoBean.getCompanies();
					if (companies.size() > 0) {
						// 有公司信息
						for (int i = 0, j = companies.size(); i < j; i++) {
							sb.append("<p>曾服役过的公司：公司ID-" + companies.get(i).getID() + " 名称-"
									+ companies.get(i).getCompanyName() + " 部门名称-"
									+ companies.get(i).getDepartmentName() + " 开始工作年-" + companies.get(i).getBeginYear()
									+ " 结束工作年-" + companies.get(i).getEndYear());
						}
					} else {
						// 没有公司信息
					}
					// 获取用户的公司信息---------------------------end

					System.out.println(sb.toString());

				} else {
					System.out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + weiboUserInfoBean.getMsg());
				}
			}
		} catch (QQConnectException e) {
			result.setMessage(e.getMessage());
		}
		return result;
	}

}
