package com.demonshrimp.dawj.model.prop;

import pers.ksy.common.PropertiesSupport;
import pers.ksy.common.annotation.Prop;
import pers.ksy.common.annotation.PropKey;

@Prop(path = "system_conf")
public class SystemConfigProperties extends PropertiesSupport {
	@PropKey
	private String uploadPath;

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

}