package com.demonshrimp.dawj.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_integral_account")
public class UserIntegralAccount extends BaseEntity {

	private User user;
	private Long points;

	@OneToOne
	@JoinColumn(name = "user_id", insertable = true, unique = true, nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(nullable = false)
	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

}
