package com.demonshrimp.dawj.web.controller.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pers.ksy.common.StringUtil;
import pers.ksy.common.wechat.WechatService;

/**
 * simple introduction
 * 
 * <p>
 * detailed comment
 * 
 * @author ksy 2015年9月13日
 * @see
 * @since 1.0
 */
@RestController(value = "wechatUserController")
@RequestMapping(value = "/usr/wechat")
public class WechatController extends BaseUserController {
	@Autowired
	private WechatService wechatService;

	@RequestMapping(value = "/jsapi-signature", method = RequestMethod.GET)
	public Object jsapiSignature(HttpServletRequest request, String url) {
		if (StringUtil.isEmpty(url)) {
			url = request.getHeader("Referer");
		}
		Map<String, String> map = wechatService.getJsapiSignature(url);
		map.remove("jsapi_ticket");
		return map;
	}
}
