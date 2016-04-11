package com.demonshrimp.dawj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.OrderDao;
import com.demonshrimp.dawj.model.entity.Order;
import com.demonshrimp.dawj.model.entity.Order.Status;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.OrderService;

@Transactional
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, String> implements OrderService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private AlipayClient alipayClient;

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
	
	public void createPayment(String id){
	}
		

	@Override
	public void pay(String orderId) {
		throw new ServiceException("not implement!");
	}

	@Override
	public void complete(String orderId) {
		throw new ServiceException("not implement!");
	}

	@Override
	public void close(String orderId) {
		throw new ServiceException("not implement!");
	}

	@Override
	protected BaseDao<Order, String> getDao() {
		return orderDao;
	}

	private void alipay() throws AlipayApiException{
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		/*String json = {
			    "out_trade_no":"20150320010101001",
			    "scene":"bar_code,wave_code",
			    "auth_code":"28763443825664394",
			    "seller_id":"2088102146225135",
			    "total_amount":88.88,
			    "discountable_amount":8.88,
			    "undiscountable_amount":80.00,
			    "subject":"Iphone6 16G",
			    "body":"Iphone6 16G",
			      "goods_detail":[{
			                "goods_id":"apple-01",
			        "alipay_goods_id":"20010001",
			        "goods_name":"ipad",
			        "quantity":1,
			        "price":2000,
			        "goods_category":"34543238",
			        "body":"特价手机"
			        }],
			    "operator_id":"yx_001",
			    "store_id":"NJ_001",
			    "terminal_id":"NJ_T_001",
			    "extend_params":{
			      "sys_service_provider_id":"2088511833207846"
			    },
			    "timeout_express":"90m",
			    "royalty_info":{
			      "royalty_type":"ROYALTY",
			        "royalty_detail_infos":[{
			                    "serial_no":1,
			          "trans_in_type":"userId",
			          "batch_no":"123",
			          "out_relation_id":"20131124001",
			          "trans_out_type":"userId",
			          "trans_out":"2088101126765726",
			          "trans_in":"2088101126708402",
			          "amount":0.1,
			          "desc":"分账测试1"
			          }]
			    }
			  }*/
		request.setBizContent("");
		AlipayTradePayResponse response = alipayClient.execute(request);
	}
	
}
