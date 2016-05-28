package com.demonshrimp.dawj.model.dao;

import com.demonshrimp.dawj.model.entity.SystemConfig;

public interface SystemConfigDao extends BaseDao<SystemConfig, String> {

	String getLicense();

	void setLicense(String license);

}