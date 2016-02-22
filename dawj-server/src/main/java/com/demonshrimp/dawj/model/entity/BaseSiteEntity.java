package com.demonshrimp.dawj.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseSiteEntity extends BaseEntity {
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "site_id", nullable = true)
	private Site site;

	public BaseSiteEntity() {
		super();
	}

	public BaseSiteEntity(String id) {
		super(id);
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

}
