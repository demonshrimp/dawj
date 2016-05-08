package com.demonshrimp.dawj.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "discount_rule")
public class DiscountRule extends BaseEntity {
	private Integer conditionAmount;
	private Integer discountAmount;

	@Column(name = "condition_amount", nullable = false)
	public Integer getConditionAmount() {
		return conditionAmount;
	}

	public void setConditionAmount(Integer conditionAmount) {
		this.conditionAmount = conditionAmount;
	}

	@Column(name = "discount_amount", nullable = false)
	public Integer getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Integer discountAmount) {
		this.discountAmount = discountAmount;
	}

}
