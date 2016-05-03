package com.demonshrimp.dawj.service;

import com.demonshrimp.dawj.model.entity.Feedback;

public interface FeedbackService extends BaseService<Feedback,String>{
	
	/**
	 * 回复反馈问题
	 * @param id
	 * @param answer 回复内容
	 */
	void answerFeedback(String id, String answer);
}
