package com.demonshrimp.dawj.model.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demonshrimp.dawj.web.controller.admin.SiteController.ImageInfo;

@Entity
@Table(name = "counselor")
public class Counselor extends BaseSiteEntity {

	private String name;
	private String title;
	private String image;
	private Double fees;
	private String about;
	private String consultingCase;
	private Boolean certificated;
	private List<CounselingType> counselingTypes;

	private String fullImage;

	public Counselor() {
		super();
	}

	public Counselor(String id) {
		super(id);
	}

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = true)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
		if (null != image && null != RequestContextHolder.getRequestAttributes()) {
			this.fullImage = ImageInfo.buildFullPath(
					((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(), image);
		}
	}

	@Column(nullable = false)
	public Double getFees() {
		return fees;
	}

	public void setFees(Double fees) {
		this.fees = fees;
	}

	@Column(nullable = false, length = 65535)
	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	@Column(name = "consulting_case", nullable = true, length = 65535)
	public String getConsultingCase() {
		return consultingCase;
	}

	public void setConsultingCase(String consultingCase) {
		this.consultingCase = consultingCase;
	}

	@Column(nullable = false)
	public Boolean getCertificated() {
		return certificated;
	}

	public void setCertificated(Boolean certificated) {
		this.certificated = certificated;
	}

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "counselor_counseling_type", joinColumns = {
			@JoinColumn(name = "counselor_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "counseling_type_id", referencedColumnName = "id") })
	public List<CounselingType> getCounselingTypes() {
		return counselingTypes;
	}

	public void setCounselingTypes(List<CounselingType> counselingTypes) {
		this.counselingTypes = counselingTypes;
	}

	@Transient
	public String getFullImage() {
		return fullImage;
	}

	public void setFullImage(String fullImage) {
		this.fullImage = fullImage;
	}

}