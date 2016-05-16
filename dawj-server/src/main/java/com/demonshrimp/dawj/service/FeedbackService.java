package com.demonshrimp.dawj.service;

import com.demonshrimp.dawj.model.entity.Feedback;

public interface FeedbackService extends BaseService<Feedback,String>{
	
	/**
	 * 回复反馈问题
	 * @param id
	 * @param answer 回复内容
	 * @param counselorId 咨询师ID
	 */
	void answerFeedback(String id, String answer, String counselorId);
}
