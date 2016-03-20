package com.demonshrimp.dawj.service;

import java.util.List;

import com.demonshrimp.dawj.model.entity.CounselingArticle;
import com.demonshrimp.dawj.model.entity.CounselingType;
import com.demonshrimp.dawj.model.entity.Counselor;
import com.demonshrimp.dawj.model.entity.Site;

import pers.ksy.common.model.Page;
import pers.ksy.common.orm.QueryCondition;

public interface CounselingService extends BaseService<Site, String> {

	/**
	 * 咨询类型列表
	 * @param siteId 所属站点
	 * @return
	 */
	public List<CounselingType> counselingTypeList(String siteId);

	/**
	 * 添加咨询类型
	 * 
	 * @param counselingType
	 * @return
	 */
	public CounselingType addCounselingType(CounselingType counselingType);

	/**
	 * 更新咨询类型
	 * @param counselingTypeId 咨询类型ID
	 * @param counselingType 咨询类型信息
	 * @return
	 */
	public CounselingType updateCounselingType(String counselingTypeId, CounselingType counselingType);

	/**
	 * 删除咨询类型
	 * @param counselingTypeId
	 * @return
	 */
	public void deleteCounselingType(String counselingTypeId);

	/**
	 * 获取资讯类型详情
	 * @param counselingTypeId 咨询类型ID
	 * @return
	 */
	public CounselingType getCounselingType(String counselingTypeId);
	
	/**
	 * 添加咨询文章
	 * @param counselingArticle
	 * @return
	 */
	CounselingArticle addCounselingArticle(CounselingArticle counselingArticle);

	/**
	 * 更新咨询文章
	 * @param counselingArticleId
	 * @param counselingArticle
	 * @return
	 */
	CounselingArticle updateCounselingArticle(String counselingArticleId, CounselingArticle counselingArticle);

	/**
	 * 删除咨询文章
	 * @param counselingArticleId
	 */
	void deleteCounselingArticle(String counselingArticleId);
	
	/**
	 * 访问咨询文章 (访问次数+1)
	 * @param counselingArticleId
	 */
	void visitCounselingArticle(String counselingArticleId);

	/**
	 * 咨询文章分页查询
	 * @param siteId
	 * @param qc
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public Page<CounselingArticle> findCounselingArticlePage(String siteId, QueryCondition qc, int pageIndex, int pageSize);

	/**
	 * 获取咨询文章详情
	 * @param counselingArticleId
	 * @return
	 */
	public CounselingArticle getCounselingArticle(String counselingArticleId);

	/**
	 * 咨询师列表
	 * @param siteId
	 * @return
	 */
	List<Counselor> counselorList(String siteId);

	/**
	 * 获取咨询师详情
	 * @param counselorId
	 * @return
	 */
	Counselor getCounselor(String counselorId);

	/**
	 * 添加咨询师
	 * @param counselor
	 * @return
	 */
	Counselor addCounselor(Counselor counselor);

	/**
	 * 更新咨询师
	 * @param counselorId
	 * @param counselor
	 * @return
	 */
	Counselor updateCounselor(String counselorId, Counselor counselor);
	
	/**
	 * 删除咨询师
	 * @param counselingTypeId
	 */
	void deleteCounselor(String counselingTypeId);

}