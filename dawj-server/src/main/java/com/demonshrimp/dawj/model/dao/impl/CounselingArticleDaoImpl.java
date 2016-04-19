package com.demonshrimp.dawj.model.dao.impl;

import org.springframework.stereotype.Repository;

import com.demonshrimp.dawj.model.dao.CounselingArticleDao;
import com.demonshrimp.dawj.model.entity.CounselingArticle;

@Repository
public class CounselingArticleDaoImpl extends BaseDaoImpl<CounselingArticle, String> implements CounselingArticleDao {

	@Override
	public CounselingArticle[] findBeforeAndAfter(String id) {
		CounselingArticle counselingArticle = load(id);
		CounselingArticle before = null;
		CounselingArticle after = null;
		String beforeId = (String) createSQLQuery(
				"select id from counseling_article where id = (select id from counseling_article where create_time < ? ORDER BY create_time desc LIMIT 0,1)",
				counselingArticle.getCreateTime()).uniqueResult();
		String afterId = (String) createSQLQuery(
				"select id from counseling_article where id = (select id from counseling_article where create_time > ? ORDER BY create_time LIMIT 0,1)",
				counselingArticle.getCreateTime()).uniqueResult();
		if (null != beforeId) {
			before = get(beforeId);
		}
		if (null != afterId) {
			after = get(afterId);
		}
		return new CounselingArticle[] { before, after };
	}
}