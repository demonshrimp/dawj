package com.demonshrimp.dawj.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.dao.CounselingTypeDao;
import com.demonshrimp.dawj.model.dao.SiteDao;
import com.demonshrimp.dawj.model.entity.CounselingType;
import com.demonshrimp.dawj.model.entity.Site;
import com.demonshrimp.dawj.service.CounselingService;

import pers.ksy.common.orm.Conditions;
import pers.ksy.common.orm.QueryCondition;

@Transactional
@Service
public class CounselingServiceImpl extends BaseServiceImpl<Site, String> implements CounselingService {

	@Autowired
	private SiteDao siteDao;
	@Autowired
	private CounselingTypeDao counselingTypeDao;

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
		c.setLastModifyTime(new Date());
		counselingTypeDao.update(c);
		return c;
	}

	@Override
	public void deleteCounselingType(String counselingTypeId) {
		counselingTypeDao.deleteById(counselingTypeId);
	}

}
