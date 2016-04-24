package com.demonshrimp.dawj.service.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pers.ksy.common.wechat.ReceiveXmlEntity;
import pers.ksy.common.wechat.WechatHandler;
import pers.ksy.common.wechat.WechatService;
import pers.ksy.common.wechat.api.WechatApiClient;
import pers.ksy.common.wechat.api.WechatJsapiSdkSignUtil;
import pers.ksy.common.wechat.api.model.AccessToken;

@Service
public class WechatServiceImpl implements WechatService, WechatHandler {
	// ACCESS_TOKEN
	private int accessTokenExpires = 7200;
	private String accessToken;
	private long accessTokenTime;
	// jsapi
	private int jsapiTicketExpires = 7200;
	private String jsapiTicket;
	private long jsapiTicketTime;

	@Autowired
	private WechatApiClient wechatApiClient;
	private Gson gson;

	@Override
	public AccessToken getAccessToken(String code) {
		AccessToken accessToken = null;
		HashMap<String, String> parames = new HashMap<String, String>();
		parames.put("code", code);
		parames.put("grant_type", "authorization_code");
		try {
			HttpResponse response = wechatApiClient.get(
					"sns/oauth2/access_token", parames);
			accessToken = getGson().fromJson(
					new InputStreamReader(response.getEntity().getContent()),
					AccessToken.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessToken;
	}
	
	@Override
	public Map<String, String> getJsapiSignature(String url) {
		return WechatJsapiSdkSignUtil.sign(getJsapiTicket(), url);
	}

	@Override
	public String massageHandle(ReceiveXmlEntity xmlEntity, String xml) {
		String result = "";
		if ("text".endsWith(xmlEntity.getMsgType())) {
			result = getTuringResult(xmlEntity.getContent());
		}
		return result;
	}

	/**
	 * 调用图灵机器人api接口，获取智能回复内容，解析获取自己所需结果
	 * 
	 * @param content
	 * @return
	 */
	public String getTuringResult(String content) {
		/** 此处为图灵api接口，参数key需要自己去注册申请，先以11111111代替 */
		String apiUrl = "http://www.tuling123.com/openapi/api?key=3fd411539cd45dbac5c50017c427d1f7&info=";
		String param = "";
		try {
			param = apiUrl + URLEncoder.encode(content, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // 将参数转为url编码

		/** 发送httpget请求 */
		HttpGet request = new HttpGet(param);
		String result = "";
		try {
			HttpResponse response = HttpClients.createDefault()
					.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/** 请求失败处理 */
		if (null == result) {
			return "对不起，你说的话真是太高深了……";
		}
		try {
			JsonObject json = new Gson().fromJson(result, JsonObject.class);
			// 以code=100000为例，参考图灵机器人api文档
			if (100000 == json.get("code").getAsInt()) {
				result = json.get("text").getAsString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取ACCESS_TOKEN
	 * 
	 * @return
	 */
	public String getAccessToken() {
		if (null == accessToken
				|| System.currentTimeMillis() - accessTokenTime >= accessTokenExpires) {
			JsonObject jsonObject = fetchAccessToken();
			if (null != jsonObject) {
				accessToken = jsonObject.get("access_token").getAsString();
				accessTokenExpires = jsonObject.get("expires_in").getAsInt();
			}
		}
		return accessToken;
	}

	/**
	 * 获取jsapi_ticket
	 * 
	 * @return
	 */
	public String getJsapiTicket() {
		if (null == jsapiTicket
				|| System.currentTimeMillis() - jsapiTicketTime >= jsapiTicketExpires) {
			JsonObject jsonObject = fetchJsapiTicket();
			if (null != jsonObject) {
				jsapiTicket = jsonObject.get("ticket").getAsString();
				jsapiTicketExpires = jsonObject.get("expires_in").getAsInt();
			}
		}
		return jsapiTicket;
	}

	/**
	 * 从微信服务器获取ACCESS_TOKEN
	 * 
	 * @return
	 */
	public JsonObject fetchAccessToken() {
		HashMap<String, String> parames = new HashMap<String, String>();
		parames.put("grant_type", "client_credential");
		try {
			HttpResponse response = wechatApiClient.get(
					"cgi-bin/token", parames);
			JsonObject jsonObject = getGson().fromJson(
					new InputStreamReader(response.getEntity().getContent()),
					JsonObject.class);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从微信服务器获取jsapi_ticket
	 * 
	 * @return
	 */
	public JsonObject fetchJsapiTicket() {
		HashMap<String, String> parames = new HashMap<String, String>();
		parames.put("type", "jsapi");
		parames.put("access_token", getAccessToken());
		try {
			HttpResponse response = wechatApiClient.get(
					"cgi-bin/ticket/getticket", parames);
			JsonObject jsonObject = getGson().fromJson(
					new InputStreamReader(response.getEntity().getContent()),
					JsonObject.class);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Gson getGson() {
		if (null == gson) {
			gson = new Gson();
		}
		return gson;
	}

}
