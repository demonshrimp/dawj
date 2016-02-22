package com.demonshrimp.dawj.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "site")
public class Site extends BaseEntity {

	private String name;
	private String city;
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
