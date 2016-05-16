package com.demonshrimp.dawj.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.FeedbackDao;
import com.demonshrimp.dawj.model.entity.Counselor;
import com.demonshrimp.dawj.model.entity.Feedback;
import com.demonshrimp.dawj.service.FeedbackService;

import pers.ksy.common.StringUtil;

@Transactional
@Service
public class FeedbackServiceImpl extends BaseServiceImpl<Feedback, String> implements FeedbackService {
	@Autowired
	private FeedbackDao feedbackDao;

	@Override
	public void answerFeedback(String id, String answer, String counselorId) {
		Feedback feedback = feedbackDao.load(id);
		feedback.setAnswer(answer);
		if (StringUtil.notEmpty(counselorId)) {
			feedback.setCounselor(new Counselor(counselorId));
		}
		feedback.setLastModifyTime(new Date());
		feedbackDao.update(feedback);
	}

	@Override
	protected BaseDao<Feedback, String> getDao() {
		return feedbackDao;
	}

}
