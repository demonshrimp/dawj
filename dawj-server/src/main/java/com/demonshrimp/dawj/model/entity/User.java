package com.demonshrimp.dawj.model.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(name = "UK_mobile", columnNames = "mobile") })
public class User extends BaseEntity {

	private UserIntegralAccount integralAccount;
	private String name;
	private String mobile;
	private String qq;
	private String wechatUserId;
	private Sex sex;
	private String password;
	private Date birthday;
	private Date lastLoginTime;

	public User() {
		super();
	}

	public User(String id) {
		super(id);
	}

	@OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public UserIntegralAccount getIntegralAccount() {
		return integralAccount;
	}

	public void setIntegralAccount(UserIntegralAccount integralAccount) {
		this.integralAccount = integralAccount;
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

	public enum Sex {
		MALE, FEMALE;
	}

}