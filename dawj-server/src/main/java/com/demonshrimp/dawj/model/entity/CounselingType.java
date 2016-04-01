package com.demonshrimp.dawj.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "counseling_type", uniqueConstraints = {
		@UniqueConstraint(name = "UK_counseling_type_name", columnNames = "name") })
public class CounselingType extends BaseSiteEntity {

	private String name;
	private String description;
	private String content;

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

	@Column(nullable = false, length = 65535)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}