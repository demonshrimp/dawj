package com.demonshrimp.dawj.model.dao.impl;

import org.springframework.stereotype.Repository;

import com.demonshrimp.dawj.model.dao.SystemConfigDao;
import com.demonshrimp.dawj.model.entity.SystemConfig;

@Repository
public class SystemConfigDaoImpl extends BaseDaoImpl<SystemConfig, String> implements SystemConfigDao {

	public static final String KEY_LICENSE = "license";

	@Override
	public String getLicense() {
		SystemConfig config = getByProperty("key", KEY_LICENSE);
		if (null != config) {
			return config.getValue();
		}
		return null;
	}

	@Override
	public void setLicense(String license) {
		SystemConfig config = new SystemConfig();
		config.setKey(KEY_LICENSE);
		config.setValue(license);
		saveOrUpdate(config);
	}
}