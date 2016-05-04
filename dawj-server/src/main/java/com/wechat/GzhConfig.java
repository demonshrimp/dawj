package com.wechat;

public class GzhConfig {
	public static String APPID = "wxc086c323ecb8c170";
	//受理商ID，身份标识
	public static String MCHID = "1315067001";
	//商户支付密钥Key。审核通过后，在微信发送的邮件中查看
	public static String KEY = "u73qa4oj48trzgn00atrbztl2jradgqw";
	//JSAPI接口中获取openid，审核后在公众平台开启开发模式后可查看
	public static String APPSECRET = "3001578fcda5d7fef7726185cd520270";
	//重定向地址
	public static String REDIRECT_URL = "http://www.dawjxlzx.com/dawj-portal/user-center.html#order";
	//异步回调地址
	public static String NOTIFY_URL = "http://www.dawjxlzx.com/dawj-server/api/admin/order/wechat-pay-callback";
	//web回调地址
	public static String WEB_NOTIFY_URL = "http://www.dawjxlzx.com/dawj-server/api/admin/order/wechat-pay-callback";
}