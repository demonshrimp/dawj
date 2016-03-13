package com.demonshrimp.dawj.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "site", uniqueConstraints = { @UniqueConstraint(name = "UK_site_name", columnNames = "name"),
		@UniqueConstraint(name = "UK_site_path", columnNames = "path") })
public class Site extends BaseEntity {

	private String name;
	private String city;
	private String path;
	private Boolean certificated;
	private Status status;
	private String password;
	private String token;
	private Date lastLoginTime;

	public Site() {
		super();
	}

	public Site(String name, String city, String path, Boolean certificated, String password) {
		super();
		this.name = name;
		this.city = city;
		this.path = path;
		this.certificated = certificated;
		this.password = password;
	}

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(nullable = false)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(nullable = false)
	public Boolean getCertificated() {
		return certificated;
	}

	public void setCertificated(Boolean certificated) {
		this.certificated = certificated;
	}

	@Column(nullable = false)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Column
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}



	public enum Status {
		DISABLED, ENABLED
	}
}
