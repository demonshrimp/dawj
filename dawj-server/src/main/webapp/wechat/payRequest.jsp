<%@page import="pers.ksy.common.wechat.api.WechatJsapiSdkSignUtil"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.SortedMap"%>
<%@page import="com.wechat.XMLUtil"%>
<%@page import="java.io.OutputStreamWriter"%>
<%@page import="com.wechat.RequestHandler"%>
<%@page import="com.demonshrimp.dawj.model.entity.Order"%>
<%@page import="pers.ksy.common.wechat.WechatService"%>
<%@page import="pers.ksy.common.spring4.SpringUtil"%>
<%@page import="com.demonshrimp.dawj.service.OrderService"%>
<%@page import="java.util.Map"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.wechat.GzhConfig"%>
<%@page import="com.wechat.Sha1Util"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.IOException"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%!
public static String getOut(HttpURLConnection conn) throws IOException{
    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
        return null;
    }
    // 获取响应内容体
    BufferedReader in = new BufferedReader(new InputStreamReader(
            conn.getInputStream(), "UTF-8"));
    String line = "";
    StringBuffer strBuf = new StringBuffer();
    while ((line = in.readLine()) != null) {
        strBuf.append(line).append("\n");
    }
    in.close();
    return  strBuf.toString().trim();
}
public static String SendGET(String url,String param){
   String result="";//访问返回结果
   BufferedReader read=null;//读取访问结果
     
   try {
    //创建url
    URL realurl=new URL(url+"?"+param);
    //打开连接
    URLConnection connection=realurl.openConnection();
     // 设置通用的请求属性
             connection.setRequestProperty("accept", "*/*");
             connection.setRequestProperty("connection", "Keep-Alive");
             connection.setRequestProperty("user-agent",
                     "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
             //建立连接
             connection.connect();
          // 获取所有响应头字段
//	         Map<String, List<String>> map = connection.getHeaderFields();
             // 遍历所有的响应头字段，获取到cookies等
//	         for (String key : map.keySet()) {
//	             System.out.println(key + "--->" + map.get(key));
//	         }
         // 定义 BufferedReader输入流来读取URL的响应
             read = new BufferedReader(new InputStreamReader(
                     connection.getInputStream(),"UTF-8"));
             String line;//循环读取
             while ((line = read.readLine()) != null) {
                 result += line;
             }
   } catch (IOException e) {
    e.printStackTrace();
   }finally{
    if(read!=null){//关闭流
     try {
      read.close();
     } catch (IOException e) {
      e.printStackTrace();
     }
    }
   }
      
   return result; 
 }
%>
<%
	String orderId = request.getParameter("orderId");
	OrderService orderService = SpringUtil.getBean(OrderService.class);
	WechatService wechatService = SpringUtil.getBean(WechatService.class);
	Order order = orderService.get(orderId);
	Gson gson = new Gson();
	
	
	
	String noncestr = Sha1Util.getNonceStr();//生成随机字符串
	String timestamp = Sha1Util.getTimeStamp();//生成1970年到现在的秒数.
	//state 可以传递你的订单号.然后根据订单号 查询付款金额.我就不详细写了.
	                                 
	String out_trade_no = order.getId();//订单号
	String order_price = String.valueOf((int) (order.getAmount() * 100));//订单金额
	//微信金额 以分为单位.这是第二坑.如果不注意.页面的报错.你基本看不出来.因为他提示系统升级,正在维护.扯淡.....
	String product_name = out_trade_no;//订单名称
	
	String openid = order.getUser().getWechatUserId();
	RequestHandler reqHandler = new RequestHandler(request, response);
	 //初始化     RequestHandler  类可以在微信的文档中找到.还有相关工具类    
	reqHandler.init();
	reqHandler.init(GzhConfig.APPID, GzhConfig.APPSECRET, GzhConfig.KEY, "");
	//执行统一下单接口 获得预支付id
	reqHandler.setParameter("appid",GzhConfig.APPID);
	reqHandler.setParameter("mch_id", GzhConfig.MCHID);                //商户号
	reqHandler.setParameter("nonce_str", noncestr);            //随机字符串
	reqHandler.setParameter("body", product_name);                        //商品描述(必填.如果不填.也会提示系统升级.正在维护我艹.)
	reqHandler.setParameter("out_trade_no", out_trade_no);        //商家订单号
	reqHandler.setParameter("total_fee", order_price);                    //商品金额,以分为单位
	reqHandler.setParameter("spbill_create_ip",request.getRemoteAddr());   //用户的公网ip  IpAddressUtil.getIpAddr(request)
	//下面的notify_url是用户支付成功后为微信调用的action  异步回调.
	reqHandler.setParameter("notify_url", GzhConfig.NOTIFY_URL);
	reqHandler.setParameter("trade_type", "JSAPI");
	//------------需要进行用户授权获取用户openid-------------
	reqHandler.setParameter("openid", openid);   //这个必填.
	//这里只是在组装数据.还没到执行到统一下单接口.因为统一下单接口的数据传递格式是xml的.所以才需要组装.
	String requestUrl = reqHandler.getRequestURL();
	                                //requestUrl 例子:
	 /*
	<xml><appid>wx2421b1c4370ec43b</appid><attach>支付测试</attach><body>JSAPI支付测试</body><mch_id>10000100</mch_id><nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str><notify_url>http://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php</notify_url><openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid><out_trade_no>1415659990</out_trade_no><spbill_create_ip>14.23.150.211</spbill_create_ip><total_fee>1</total_fee><trade_type>JSAPI</trade_type><sign>0CB01533B8C1EF103065174F50BCA001</sign></xml>
	*/
	                                 
	System.out.println("requestUrl==================="+requestUrl);
	//统一下单接口提交  xml格式
	URL orderUrl = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");
	HttpURLConnection conn = (HttpURLConnection) orderUrl.openConnection();
	conn.setConnectTimeout(30000); // 设置连接主机超时（单位：毫秒)
	conn.setReadTimeout(30000); // 设置从主机读取数据超时（单位：毫秒)
	conn.setDoOutput(true); // post请求参数要放在http正文内，顾设置成true，默认是false
	conn.setDoInput(true); // 设置是否从httpUrlConnection读入，默认情况下是true
	conn.setUseCaches(false); // Post 请求不能使用缓存
	// 设定传送的内容类型是可序列化的java对象(如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
	conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	conn.setRequestMethod("POST");// 设定请求的方法为"POST"，默认是GET
	conn.setRequestProperty("Content-Length",requestUrl.length()+"");
	String encode = "utf-8";
	OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), encode);
	os.write(requestUrl.toString());
	os.flush();
	os.close();
	String result = getOut(conn);
	System.out.println("result=========返回的xml============="+result);
	Map<String, String> orderMap = XMLUtil.doXMLParse(result);
	System.out.println("orderMap==========================="+orderMap);
	//得到的预支付id
	String prepay_id = orderMap.get("prepay_id");
	SortedMap<String,String> params = new TreeMap<String,String>();
	params.put("appId", GzhConfig.APPID);
	params.put("timeStamp",timestamp);
	params.put("nonceStr", noncestr);
	params.put("package", "prepay_id="+prepay_id);
	params.put("signType", "MD5");
	         
	//生成支付签名,这个签名 给 微信支付的调用使用
	String paySign =  reqHandler.createSign(params);        
	request.setAttribute("paySign", paySign);
	request.setAttribute("appId", GzhConfig.APPID);
	request.setAttribute("timeStamp", timestamp);        //时间戳
	request.setAttribute("nonceStr", noncestr);            //随机字符串
	request.setAttribute("signType", "MD5");        //加密格式
	request.setAttribute("out_trade_no", out_trade_no);          //订单号
	request.setAttribute("packageValue", "prepay_id="+prepay_id);//预支付id ,就这样的格式.
	
	//生成js api签名
	String url = "http://www.dawjxlzx.com/dawj-server/wechat/payRequest.jsp";
	String ticket = wechatService.getJsapiTicket();
	String signature = WechatJsapiSdkSignUtil.sign(ticket, url).get("signature");
	request.setAttribute("signature", signature);
%>

<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script language="javascript">
//加载
wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: '${requestScope.appId}', // 必填，公众号的唯一标识
    timestamp: '${requestScope.timeStamp}', // 必填，生成签名的时间戳
    nonceStr: '${requestScope.nonceStr}', // 必填，生成签名的随机串
    signature: '${requestScope.signature}',// 必填，签名，见附录1
    jsApiList: [ 'checkJsApi', 'chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});

wx.ready(function() {
	//支付
	wx.chooseWXPay({
		timestamp: '${requestScope.timeStamp}', // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
	    nonceStr: '${requestScope.nonceStr}', // 支付签名随机串，不长于 32 位
	    package: '${requestScope.packageValue}', // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
	    signType: '${requestScope.signType}', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
	    paySign: '${requestScope.paySign}', // 支付签名
		success : function(res) {
			// 支付成功后的回调函数
			WeixinJSBridge.log(res.err_msg);
			//alert("支付接口:"+res.err_code + res.err_desc + res.err_msg);
			if (!res.err_msg) {
				//支付完后.跳转到成功页面.
				location.href = "http://www.dawjxlzx.com/user-center.html#order";
			}
		}
	});
	// config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
});
wx.error(function(res) {
	// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
});
wx.checkJsApi({
	jsApiList : [ 'chooseWXPay' ], // 需要检测的JS接口列表，所有JS接口列表见附录2,
	success : function(res) {
		//alert("检测接口:"+res.err_msg);
	}
});
</script>
