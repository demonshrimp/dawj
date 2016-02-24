package com.demonshrimp.dawj.service;

import java.util.List;

import com.demonshrimp.dawj.model.entity.CounselingType;
import com.demonshrimp.dawj.model.entity.Site;

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

}