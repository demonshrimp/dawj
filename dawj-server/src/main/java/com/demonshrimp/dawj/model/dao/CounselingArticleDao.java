package com.demonshrimp.dawj.model.dao;

import com.demonshrimp.dawj.model.entity.CounselingArticle;

public interface CounselingArticleDao extends BaseDao<CounselingArticle, String> {

	/**
	 * 查询一篇文章的前后记录
	 * @param id
	 * @return
	 */
	CounselingArticle[] findBeforeAndAfter(String id);
}