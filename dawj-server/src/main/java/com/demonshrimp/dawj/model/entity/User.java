package com.demonshrimp.dawj.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(name = "UK_mobile", columnNames = "mobile") })
public class User extends BaseSiteEntity {

	private String name;
	private String mobile;
	private String qq;
	private String wechatUserId;
	private Sex sex;
	private String password;
	private Date birthday;
	private Date lastLoginTime;
	private Integer points;

	public User() {
		super();
	}

	public User(String id) {
		super(id);
	}

	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column
	public String getWechatUserId() {
		return wechatUserId;
	}

	public void setWechatUserId(String wechatUserId) {
		this.wechatUserId = wechatUserId;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(nullable = true)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "last_login_time", length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Column(nullable = false)
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public enum Sex {
		MALE, FEMALE;
	}

}