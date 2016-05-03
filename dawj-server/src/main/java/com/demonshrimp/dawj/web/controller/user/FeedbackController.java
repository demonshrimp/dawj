package com.demonshrimp.dawj.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.Feedback;
import com.demonshrimp.dawj.service.FeedbackService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;
import pers.ksy.common.orm.Order;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.QueryConditionImpl;

@RestController(value = "feedbackUserController")
@RequestMapping(value = "/usr/feedback")
public class FeedbackController extends BaseUserController {
	@Autowired
	private FeedbackService feedbackService;

	@RequestMapping(path = "/page", method = RequestMethod.GET)
	@SerializationFilter
	public Object page(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "length") int pageSize) {
		QueryCondition qc = new QueryConditionImpl(Feedback.class);
		qc.addOrder(Order.desc("createTime"));
		return Result.successResult(feedbackService.findByPage(qc, pageIndex, pageSize), null);
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Object save(@RequestBody Feedback feedback) {
		feedback.setUser(getCurrentUser(false));
		feedbackService.save(feedback);
		return Result.successResult("新增成功");
	}

}
