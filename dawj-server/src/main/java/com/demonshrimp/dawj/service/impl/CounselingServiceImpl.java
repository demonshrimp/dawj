package com.demonshrimp.dawj.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.CounselingArticleDao;
import com.demonshrimp.dawj.model.dao.CounselingTypeDao;
import com.demonshrimp.dawj.model.dao.CounselorDao;
import com.demonshrimp.dawj.model.dao.SiteDao;
import com.demonshrimp.dawj.model.entity.CounselingArticle;
import com.demonshrimp.dawj.model.entity.CounselingType;
import com.demonshrimp.dawj.model.entity.Counselor;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.CounselingService;

import pers.ksy.common.model.Page;
import pers.ksy.common.orm.Conditions;
import pers.ksy.common.orm.QueryCondition;

@Transactional
@Service
public class CounselingServiceImpl extends BaseServiceImpl<Site, String> implements CounselingService {

	@Autowired
	private SiteDao siteDao;
	@Autowired
	private CounselingTypeDao counselingTypeDao;
	@Autowired
	private CounselingArticleDao counselingArticleDao;
	@Autowired
	private CounselorDao counselorDao;

	@Override
	public BaseDao<Site, String> getDao() {
		return siteDao;
	}

	@Override
	public List<CounselingType> counselingTypeList(String siteId) {
		QueryCondition qc = counselingTypeDao.getQC();
		qc.add(Conditions.eq("site.id", siteId));
		return counselingTypeDao.listByQC(qc);
	}

	@Override
	public CounselingType getCounselingType(String counselingTypeId) {
		return counselingTypeDao.get(counselingTypeId);
	}
	
	@Override
	public CounselingType addCounselingType(CounselingType counselingType) {
		counselingType.setCreateTime(new Date());
		counselingTypeDao.save(counselingType);
		return counselingType;
	}

	@Override
	public CounselingType updateCounselingType(String counselingTypeId, CounselingType counselingType) {
		CounselingType c = counselingTypeDao.get(counselingTypeId);
		c.setName(counselingType.getName());
		c.setDescription(counselingType.getDescription());
		c.setContent(counselingType.getContent());
		c.setLastModifyTime(new Date());
		counselingTypeDao.update(c);
		return c;
	}

	@Override
	public void deleteCounselingType(String counselingTypeId) {
		counselingTypeDao.deleteById(counselingTypeId);
	}


	@Override
	public CounselingArticle addCounselingArticle(CounselingArticle counselingArticle) {
		counselingArticle.setCreateTime(new Date());
		counselingArticle.setVisited((long)0);
		counselingArticleDao.save(counselingArticle);
		return counselingArticle;
	}

	@Override
	public CounselingArticle updateCounselingArticle(String counselingArticleId, CounselingArticle counselingArticle) {
		CounselingArticle c = counselingArticleDao.get(counselingArticleId);
		c.setCounselingType(counselingArticle.getCounselingType());
		c.setTitle(counselingArticle.getTitle());
		c.setContent(counselingArticle.getContent());
		c.setDescription(counselingArticle.getDescription());
		c.setImage(counselingArticle.getImage());
		c.setTag(counselingArticle.getTag());
		c.setLastModifyTime(new Date());
		counselingArticleDao.update(c);
		return c;
	}

	@Override
	public void deleteCounselingArticle(String counselingArticleId) {
		counselingArticleDao.deleteById(counselingArticleId);
	}
	
	@Override
	public void visitCounselingArticle(String counselingArticleId){
		CounselingArticle counselingArticle = counselingArticleDao.get(counselingArticleId);
		counselingArticle.setVisited(counselingArticle.getVisited()+1);
		counselingArticleDao.update(counselingArticle);
	}

	@Override
	public Page<CounselingArticle> findCounselingArticlePage(String siteId, QueryCondition qc, int pageIndex,
			int pageSize) {
		qc.eq("site.id", siteId);
		return counselingArticleDao.findByPage(qc, pageIndex, pageSize);
	}

	
	@Override
	public CounselingArticle getCounselingArticle(String counselingArticleId) {
		return counselingArticleDao.get(counselingArticleId);
	}
	
	
	@Override
	public List<Counselor> counselorList(String siteId) {
		QueryCondition qc = counselorDao.getQC();
		qc.add(Conditions.eq("site.id", siteId));
		return counselorDao.listByQC(qc);
	}

	@Override
	public Counselor getCounselor(String counselorId) {
		return counselorDao.get(counselorId);
	}
	
	@Override
	public Counselor addCounselor(Counselor counselor) {
		counselor.setCreateTime(new Date());
		counselorDao.save(counselor);
		return counselor;
	}

	@Override
	public Counselor updateCounselor(String counselorId, Counselor counselor) {
		Counselor c = counselorDao.get(counselorId);
		c.setName(counselor.getName());
		c.setTitle(counselor.getTitle());
		c.setAbout(counselor.getAbout());
		c.setConsultingCase(counselor.getConsultingCase());
		c.setImage(counselor.getImage());
		c.setLastModifyTime(new Date());
		c.setCounselingTypes(counselor.getCounselingTypes());
		counselorDao.update(c);
		return c;
	}

	@Override
	public void deleteCounselor(String counselingTypeId) {
		counselorDao.deleteById(counselingTypeId);
	}

	@Override
	public Page<Counselor> findCounselorPage(String siteId, QueryCondition qc, int pageIndex, int pageSize) {
		qc.eq("site.id", siteId);
		return counselorDao.findByPage(qc, pageIndex, pageSize);
	}

	
	
}
