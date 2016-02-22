package com.demonshrimp.dawj.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "site", uniqueConstraints = { @UniqueConstraint(name = "UK_site_path", columnNames = "path") })
public class Site extends BaseEntity {

	private String name;
	private String city;
	private String path;
	private Boolean certificated;
	private String password;

	@Column(nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable=false)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(nullable=false)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(nullable=false)
	public Boolean getCertificated() {
		return certificated;
	}

	public void setCertificated(Boolean certificated) {
		this.certificated = certificated;
	}

	@Column(nullable=false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
