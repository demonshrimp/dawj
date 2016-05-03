package com.demonshrimp.dawj.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demonshrimp.dawj.model.entity.CounselingArticle;
import com.demonshrimp.dawj.model.entity.Feedback;
import com.demonshrimp.dawj.service.FeedbackService;

import pers.ksy.common.annotation.SerializationFilter;
import pers.ksy.common.model.Result;
import pers.ksy.common.orm.Order;
import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.QueryConditionImpl;

@RestController(value = "feedbackAdminController")
@RequestMapping(value = "/admin/feedback")
public class FeedbackController extends BaseAdminController {
	@Autowired
	private FeedbackService feedbackService;

	@RequestMapping(path = "/page", method = RequestMethod.GET)
	@SerializationFilter
	public Object page(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "length") int pageSize) {
		QueryCondition qc = new QueryConditionImpl(Feedback.class);
		qc.addOrder(Order.desc("createTime"));
		return Result.successResult(feedbackService.findByPage(qc, pageIndex, pageSize), null);
	}

	@RequestMapping(path = "/{id}/answer", method = RequestMethod.PUT)
	public Object answer(@PathVariable String id, String answer) {
		feedbackService.answerFeedback(id, answer);
		return Result.successResult("回复成功");
	}

}
