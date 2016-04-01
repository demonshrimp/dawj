package com.demonshrimp.dawj.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demonshrimp.dawj.web.controller.admin.SiteController.ImageInfo;

@Entity
@Table(name = "counseling_article")
public class CounselingArticle extends BaseSiteEntity {

	private CounselingType counselingType;
	private String title;
	private String content;
	private String description;
	private String image;
	private String tag;
	private Long visited;
	
	private String fullImage;

	public CounselingArticle() {
		super();
	}

	public CounselingArticle(String id) {
		super(id);
	}

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "counseling_type_id", nullable = false)
	public CounselingType getCounselingType() {
		return counselingType;
	}

	public void setCounselingType(CounselingType counselingType) {
		this.counselingType = counselingType;
	}

	@Column(nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false, length = 65535)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(nullable = false)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
		this.fullImage = ImageInfo.buildFullPath(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(), image);
	}

	@Column(nullable = true)
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Column
	public Long getVisited() {
		return visited;
	}

	public void setVisited(Long visited) {
		this.visited = visited;
	}

	@Transient
	public String getFullImage() {
		return fullImage;
	}

	public void setFullImage(String fullImage) {
		this.fullImage = fullImage;
	}

	
}