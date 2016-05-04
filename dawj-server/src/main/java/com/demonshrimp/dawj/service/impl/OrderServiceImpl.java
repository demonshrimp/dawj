package com.demonshrimp.dawj.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.OrderDao;
import com.demonshrimp.dawj.model.entity.Order;
import com.demonshrimp.dawj.model.entity.Order.PaymentPlatform;
import com.demonshrimp.dawj.model.entity.Order.Status;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.OrderService;

import pers.ksy.common.MD5Util;
import pers.ksy.common.wechat.WechatService;

@Transactional
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, String> implements OrderService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private WechatService wechatService;

	/*
	 * @Autowired private AlipayClient alipayClient;
	 */

	@Override
	public Order addOrder(Order order, String siteId) {
		order.setSite(new Site(siteId));
		order.setStatus(Status.NEW);
		return save(order);
	}

	@Override
	public Order delete(String id) {
		throw new ServiceException("not implement!");
	}

	@Override
	public void pay(String orderId, String thirdOrderId, PaymentPlatform paymentPlatform) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.NEW) {
			throw new ServiceException("不能对该状态的订单进行付款操作");
		}
		order.setThirdOrderId(thirdOrderId);
		order.setPaymentPlatform(paymentPlatform);
		order.setPaymentTime(new Date());
		order.setStatus(Order.Status.PAID);
		orderDao.update(order);
	}

	@Override
	public void complete(String orderId) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.PAID) {
			throw new ServiceException("不能对该状态的订单进行完成操作");
		}
		order.setFulfillmentTime(new Date());
		order.setStatus(Order.Status.COMPLETED);
		orderDao.update(order);
	}

	@Override
	public void close(String orderId) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.NEW) {
			throw new ServiceException("不能对该状态的订单进行关闭操作");
		}
		order.setCloseTime(new Date());
		order.setStatus(Order.Status.CLOSED);
		orderDao.update(order);
	}

	@Override
	public void refundApply(String orderId) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.PAID) {
			throw new ServiceException("不能对该状态的订单进行退款申请操作");
		}
		order.setRefundApplyTime(new Date());
		order.setStatus(Order.Status.REFUNDABLE);
		orderDao.update(order);
	}

	@Override
	public void refund(String orderId) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.REFUNDABLE) {
			throw new ServiceException("不能对该状态的订单进行退款操作");
		}
		/*
		 * switch (order.getPaymentPlatform()) { case ALIPAY:
		 * refundFromAlipay(order); break; default: throw new
		 * ServiceException(order.getPaymentPlatform() + "平台暂不支持退款操作"); }
		 */
		order.setCloseTime(new Date());
		order.setStatus(Order.Status.CLOSED);
		orderDao.update(order);
	}

	@Override
	public long[] statisticsQuantityMonthly(String siteId, int year) {
		long[] result = new long[12];
		List<Object[]> list = orderDao.statisticsQuantityMonthly(siteId, year);
		for (Object[] data : list) {
			long number = Long.valueOf(data[0].toString());
			int month = Integer.valueOf(data[1].toString());
			result[month - 1] = number;
		}
		return result;
	}

	@Override
	public void selfOrderCheck(String orderId, String userId) {
		Order order = orderDao.load(orderId);
		if (order.getUser().getId() != userId) {
			throw new ServiceException("非法操作其他订单");
		}
	}

	@Override
	public String createWechatPayment(String orderId) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.NEW) {
			throw new ServiceException("不能对该状态的订单进行付款操作");
		}
		// 组装参数
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		TreeMap<String, String> params = new TreeMap<>();
		params.put("appid", "wxc086c323ecb8c170");
		params.put("mch_id", "1315067001");
		params.put("out_trade_no", order.getId());
		params.put("device_info", order.getSite().getId());
		params.put("body", "zixunshifuwu");
		params.put("product_id", order.getCounselor().getId());
		params.put("total_fee", (int) (order.getAmount() * 100) + "");
		params.put("spbill_create_ip", request.getServerName());
		params.put("notify_url",
				"http://" + request.getServerName() + "/dawj-server/api/admin/order/wechat-pay-callback");
		params.put("trade_type", "NATIVE");
		params.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));

		// 计算sign
		String sign = wechatSign(params);
		// 构建xml参数
		Document requestDocument = DocumentHelper.createDocument();
		// 创建文档的 根元素节点
		Element requestRoot = DocumentHelper.createElement("xml");
		requestDocument.setRootElement(requestRoot);
		for (String key : params.keySet()) {
			requestRoot.addElement(key).setText(params.get(key));
		}
		requestRoot.addElement("sign").setText(sign);

		HttpClient httpClient = new HttpClient();
		PostMethod method = new PostMethod("https://api.mch.weixin.qq.com/pay/unifiedorder");
		try {
			method.setRequestBody(new String(requestRoot.asXML().getBytes(), "ISO8859-1"));
			httpClient.executeMethod(method);
			String responseStr = new String(method.getResponseBody(), "utf-8");
			System.out.println(responseStr);
			Document responseDocument = DocumentHelper.parseText(responseStr);
			Element responseRoot = responseDocument.getRootElement();
			String return_code = responseRoot.element("return_code").getText();
			if (!return_code.equals("SUCCESS")) {
				throw new ServiceException(
						"微信交易创建失败:" + return_code + "," + responseRoot.element("return_msg").getText());
			}
			return responseRoot.element("code_url").getText();
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException("微信交易创建失败:" + e.getMessage());
		}
	}

	@Override
	public Map<String, String> getPaySignature(String orderId) {
		Order order = orderDao.load(orderId);
		if (order.getStatus() != Status.NEW) {
			throw new ServiceException("不能对该状态的订单进行付款操作");
		}
		// 组装参数
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		TreeMap<String, String> contentMap = new TreeMap<>();
		contentMap.put("appid", "wxc086c323ecb8c170");
		contentMap.put("mch_id", "1315067001");
		contentMap.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));
		contentMap.put("body", "zixunshifuwu");
		contentMap.put("out_trade_no", order.getId());
		/*
		 * params.put("device_info", order.getSite().getId());
		 * params.put("product_id", order.getCounselor().getId());
		 */
		contentMap.put("total_fee", (int) (order.getAmount() * 100) + "");
		contentMap.put("spbill_create_ip", request.getServerName());
		contentMap.put("notify_url",
				"http://" + request.getServerName() + "/dawj-server/api/admin/order/wechat-pay-callback");
		contentMap.put("trade_type", "JSAPI");
		contentMap.put("openid", order.getUser().getWechatUserId());

		// 计算sign
		String sign = wechatSign(contentMap);
		// 构建xml参数
		Document requestDocument = DocumentHelper.createDocument();
		// 创建文档的 根元素节点
		Element requestRoot = DocumentHelper.createElement("xml");
		requestDocument.setRootElement(requestRoot);
		for (String key : contentMap.keySet()) {
			requestRoot.addElement(key).setText(contentMap.get(key));
		}
		requestRoot.addElement("sign").setText(sign);
		HttpClient httpClient = new HttpClient();
		PostMethod method = new PostMethod("https://api.mch.weixin.qq.com/pay/unifiedorder");
		try {
			method.setRequestBody(new String(requestRoot.asXML().getBytes(), "ISO8859-1"));
			httpClient.executeMethod(method);
			String responseStr = new String(method.getResponseBody(), "utf-8");
			System.out.println(responseStr);
			Document responseDocument = DocumentHelper.parseText(responseStr);
			Element responseRoot = responseDocument.getRootElement();
			String return_code = responseRoot.element("return_code").getText();
			if (!return_code.equals("SUCCESS")) {
				throw new ServiceException(
						"微信交易创建失败:" + return_code + "," + responseRoot.element("return_msg").getText());
			}
			String prepayId = responseRoot.element("prepay_id").getText();

			TreeMap<String, String> wxPayParamMap = new TreeMap<String, String>();
			wxPayParamMap.put("appId", "wxc086c323ecb8c170");
			wxPayParamMap.put("timeStamp", String.valueOf(System.currentTimeMillis()));
			wxPayParamMap.put("nonceStr", contentMap.get("nonceStr"));
			wxPayParamMap.put("package", "prepay_id=" + prepayId);
			wxPayParamMap.put("signType", "MD5");

			String paySign = wechatSign(wxPayParamMap);
			wxPayParamMap.put("paySign", paySign);
			return wxPayParamMap;
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException("微信交易创建失败:" + e.getMessage());
		}
	}

	/*
	 * public void test(){ HttpServletRequest request; RequestHandler reqHandler
	 * = new RequestHandler(request, response); //初始化 RequestHandler
	 * 类可以在微信的文档中找到.还有相关工具类 reqHandler.init(); reqHandler.init(GzhConfig.APPID,
	 * GzhConfig.APPSECRET, GzhConfig.KEY, ""); //执行统一下单接口 获得预支付id
	 * reqHandler.setParameter("appid",GzhConfig.APPID);
	 * reqHandler.setParameter("mch_id", GzhConfig.MCHID); //商户号
	 * reqHandler.setParameter("nonce_str", noncestr); //随机字符串
	 * reqHandler.setParameter("body", product_name);
	 * //商品描述(必填.如果不填.也会提示系统升级.正在维护我艹.) reqHandler.setParameter("out_trade_no",
	 * out_trade_no); //商家订单号 reqHandler.setParameter("total_fee", order_price);
	 * //商品金额,以分为单位
	 * reqHandler.setParameter("spbill_create_ip",request.getRemoteAddr());
	 * //用户的公网ip IpAddressUtil.getIpAddr(request)
	 * //下面的notify_url是用户支付成功后为微信调用的action 异步回调.
	 * reqHandler.setParameter("notify_url", GzhConfig.NOTIFY_URL);
	 * reqHandler.setParameter("trade_type", "JSAPI");
	 * //------------需要进行用户授权获取用户openid-------------
	 * reqHandler.setParameter("openid", openid); //这个必填.
	 * //这里只是在组装数据.还没到执行到统一下单接口.因为统一下单接口的数据传递格式是xml的.所以才需要组装. String requestUrl
	 * = reqHandler.getRequestURL(); }
	 */

	/**
	 * 
	 * @param wechatResult
	 *            示例： <xml><appid><![CDATA[wxc086c323ecb8c170]]></appid>
	 *            <bank_type><![CDATA[CFT]]></bank_type>
	 *            <cash_fee><![CDATA[1]]></cash_fee>
	 *            <device_info><![CDATA[root_site]]></device_info>
	 *            <fee_type><![CDATA[CNY]]></fee_type>
	 *            <is_subscribe><![CDATA[Y]]></is_subscribe>
	 *            <mch_id><![CDATA[1315067001]]></mch_id>
	 *            <nonce_str><![CDATA[8e3b4545841348739dc3764bf8bdcf1c]]>
	 *            </nonce_str>
	 *            <openid><![CDATA[of_lRwp74TYQX-BGs8wPFYMd6ZNU]]></openid>
	 *            <out_trade_no><![CDATA[e5499fb55423ab19015423ad21690000]]>
	 *            </out_trade_no> <result_code><![CDATA[SUCCESS]]></result_code>
	 *            <return_code><![CDATA[SUCCESS]]></return_code>
	 *            <sign><![CDATA[199E9EE0054953DE96AC10952CB2794C]]></sign>
	 *            <time_end><![CDATA[20160417180405]]></time_end>
	 *            <total_fee>1</total_fee>
	 *            <trade_type><![CDATA[NATIVE]]></trade_type>
	 *            <transaction_id><![CDATA[4006082001201604174935897990]]>
	 *            </transaction_id> </xml>
	 */
	@Override
	public void wechatPayComplete(String wechatResult) {
		try {
			Document responseDocument = DocumentHelper.parseText(wechatResult);
			Element responseRoot = responseDocument.getRootElement();
			String return_code = responseRoot.element("return_code").getText();
			if (!return_code.equals("SUCCESS")) {
				return;
			}
			String sign = responseRoot.element("sign").getText();
			String out_trade_no = responseRoot.element("out_trade_no").getText();
			String transaction_id = responseRoot.element("transaction_id").getText();

			Iterator<Element> iterator = responseRoot.elementIterator();
			TreeMap<String, String> params = new TreeMap<>();
			while (iterator.hasNext()) {
				Element element = iterator.next();
				if (!element.getName().equals("sign")) {
					params.put(element.getName(), element.getText());
				}
			}
			if (!sign.equals(wechatSign(params))) {
				return;
			}

			// 校验通过开始业务操作
			pay(out_trade_no, transaction_id, PaymentPlatform.WECHAT);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected BaseDao<Order, String> getDao() {
		return orderDao;
	}

	// state methods

	/**
	 * 支付宝退款
	 * 
	 * @throws AlipayApiException
	 */
	/*
	 * private void refundFromAlipay(Order order) { AlipayTradeRefundRequest
	 * request = new AlipayTradeRefundRequest(); JsonObject jsonObject = new
	 * JsonObject(); jsonObject.addProperty("out_trade_no", order.getId());
	 * jsonObject.addProperty("trade_no", order.getThirdOrderId());
	 * jsonObject.addProperty("refund_amount", order.getAmount());
	 * request.setBizContent(jsonObject.toString()); try {
	 * AlipayTradeRefundResponse response = alipayClient.execute(request);
	 * JsonObject responseJson = new Gson().fromJson(response.getBody(),
	 * JsonObject.class); responseJson =
	 * responseJson.getAsJsonObject("alipay_trade_refund_response");
	 * System.out.println(responseJson); if
	 * (responseJson.get("code").getAsString() != "10000") { throw new
	 * ServiceException("支付宝退款失败：" + responseJson.get("msg") +
	 * responseJson.get("sub_msg")); } } catch (AlipayApiException e) {
	 * e.printStackTrace(); throw new ServiceException("支付宝退款失败：" +
	 * e.getMessage()); } }
	 */

	private String wechatSign(TreeMap<String, String> params) {
		// 计算sign
		String pramStr = getUrl(params, "utf-8");
		pramStr += "&key=u73qa4oj48trzgn00atrbztl2jradgqw";
		String sign = MD5Util.MD5(pramStr).toUpperCase();
		return sign;
	}

	/**
	 * 据Map生成URL字符串
	 * 
	 * @param map
	 *            Map
	 * @param valueEnc
	 *            URL编码
	 * @return URL
	 */
	private final static String EMPTY = "";
	private static final String URL_PARAM_CONNECT_FLAG = "&";

	private static String getUrl(Map<String, String> map, String valueEnc) {
		if (null == map || map.keySet().size() == 0) {
			return (EMPTY);
		}
		StringBuffer url = new StringBuffer();
		Set<String> keys = map.keySet();
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			String key = it.next();
			if (map.containsKey(key)) {
				String val = map.get(key);
				String str = val != null ? val : EMPTY;
				/*
				 * try { str = URLEncoder.encode(str, valueEnc); } catch
				 * (UnsupportedEncodingException e) { e.printStackTrace(); }
				 */
				url.append(key).append("=").append(str).append(URL_PARAM_CONNECT_FLAG);
			}
		}
		String strURL = EMPTY;
		strURL = url.toString();
		if (URL_PARAM_CONNECT_FLAG.equals(EMPTY + strURL.charAt(strURL.length() - 1))) {
			strURL = strURL.substring(0, strURL.length() - 1);
		}
		return (strURL);
	}

}
