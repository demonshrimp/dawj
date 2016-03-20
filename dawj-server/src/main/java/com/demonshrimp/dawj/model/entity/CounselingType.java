package com.demonshrimp.dawj.model.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "counseling_type", uniqueConstraints = {
		@UniqueConstraint(name = "UK_counseling_type_name", columnNames = "name") })
public class CounselingType extends BaseSiteEntity {

	private String name;
	private String description;

	public CounselingType() {
		super();
	}

	public CounselingType(String id) {
		super(id);
	}

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}