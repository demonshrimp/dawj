package com.demonshrimp.dawj.message.sms.weimi;

import java.util.HashMap;
import java.util.Map;

public class WeiMiSmsProvider {
	private String uid;
	private String pas;

	
	public String sendCaptcha(String mobile, int captcha) {
		Map<String, String> map = new HashMap<>(1);
		map.put("p1", captcha + "");
		map.put("p2", "");
		return sms_api2(mobile, "json", "EQIFMN2x1FnH", map);
	}
	
	/**
	 * 短信接口一，自写短信内容。该接口提交的短信均由人工审核，下发后请联系在线客服。适合：节假日祝福、会员营销群发等。
	 */
	public String sms_api1(String mobiles, String content, String type) {
		Map<String, String> para = new HashMap<String, String>();
		/**
		 * 目标手机号码，多个以“,”分隔，一次性调用最多100个号码，示例：139********,138********
		 */
		para.put("mob", mobiles);
		/**
		 * 微米账号的接口UID
		 */
		para.put("uid", uid);
		/**
		 * 微米账号的接口密码
		 */
		para.put("pas", pas);
		/**
		 * 接口返回类型：json、xml、txt。默认值为txt
		 */
		para.put("type", type);
		/**
		 * 短信内容。必须设置好短信签名，签名规范： 1、短信内容一定要带签名，签名放在短信内容的最前面；
		 * 2、签名格式：【***】，签名内容为三个汉字以上（包括三个）； 3、短信内容不允许双签名，即短信内容里只有一个“【】”
		 * 
		 */
		para.put("con", content);

		/**
		 * 特别注意：参数传递时去除“<>”符号！
		 */
		try {
			return HttpClientHelper
					.convertStreamToString(HttpClientHelper.post("http://api.weimi.cc/2/sms/send.html", para), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 短信接口二，触发类模板短信接口，可以设置动态参数变量。适合：验证码、订单短信等。
	 */
	public String sms_api2(String mobiles, String type, String cid, Map<String, String> param) {
		Map<String, String> para = new HashMap<String, String>();
		/**
		 * 目标手机号码，多个以“,”分隔，一次性调用最多100个号码，示例：139********,138********
		 */
		para.put("mob", mobiles);
		/**
		 * 微米账号的接口UID
		 */
		para.put("uid", uid);
		/**
		 * 微米账号的接口密码
		 */
		para.put("pas", pas);
		/**
		 * 接口返回类型：json、xml、txt。默认值为txt
		 */
		para.put("type", type);
		/**
		 * 短信模板cid，通过微米后台创建，由在线客服审核。必须设置好短信签名，签名规范： 1、模板内容一定要带签名，签名放在模板内容的最前面；
		 * 2、签名格式：【***】，签名内容为三个汉字以上（包括三个）； 3、短信内容不允许双签名，即短信内容里只有一个“【】”
		 */
		para.put("cid", cid);
		/**
		 * 传入模板参数。
		 * 
		 * 短信模板示例： 【微米】您的验证码是：%P%，%P%分钟内有效。如非您本人操作，可忽略本消息。
		 * 
		 * 传入两个参数： p1：610912 p2：3 最终发送内容：
		 * 【微米】您的验证码是：610912，3分钟内有效。如非您本人操作，可忽略本消息。
		 */
		para.putAll(param);

		try {
			return HttpClientHelper
					.convertStreamToString(HttpClientHelper.post("http://api.weimi.cc/2/sms/send.html", para), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPas() {
		return pas;
	}

	public void setPas(String pas) {
		this.pas = pas;
	}

	public static void main(String[] a) {
		WeiMiSmsProvider smsProvider = new WeiMiSmsProvider();
		smsProvider.uid = "iEZjuUjzFSG7";
		smsProvider.pas = "etfuny4g";
		smsProvider.sms_api1("18589005467", "【大爱无疆】尊敬的用户，你的手机验证码是：12346，30分钟内有效。请不要把此验证码泄露给任何人，以便你能安全使用。", "json");
	}
}
