package com.demonshrimp.dawj.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "system_config")
public class SystemConfig implements Serializable {
	private String key;
	private String value;

	public SystemConfig() {
		super();
	}

	public SystemConfig(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	@Id
	@Column(name = "config_key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	@Column(name = "config_value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
