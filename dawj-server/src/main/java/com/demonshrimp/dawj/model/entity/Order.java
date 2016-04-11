package com.demonshrimp.dawj.model.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "counseling_order")
public class Order extends BaseSiteEntity {

	private User user;
	private Counselor counselor;
	private CounselingType counselingType;
	private String consultingContent;
	private Integer consultingDuration;
	private Double amount;
	private String contactName;
	private String contactMobile;
	private String contactAddress;
	private String remark;
	private Date scheduledTime;
	private Date paymentTime;
	private Date fulfillmentTime;
	private Status status;

	public Order() {
		super();
	}

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "counselor_id", nullable = false)
	public Counselor getCounselor() {
		return counselor;
	}

	public void setCounselor(Counselor counselor) {
		this.counselor = counselor;
	}

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "counseling_type_id", nullable = false)
	public CounselingType getCounselingType() {
		return counselingType;
	}

	public void setCounselingType(CounselingType counselingType) {
		this.counselingType = counselingType;
	}

	@Column(name = "consulting_content", nullable = false)
	public String getConsultingContent() {
		return consultingContent;
	}

	public void setConsultingContent(String consultingContent) {
		this.consultingContent = consultingContent;
	}

	@Column(name = "consulting_duration", nullable = false)
	public Integer getConsultingDuration() {
		return consultingDuration;
	}

	public void setConsultingDuration(Integer consultingDuration) {
		this.consultingDuration = consultingDuration;
	}

	@Column(nullable = false, scale = 2)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Column(name = "contact_name", nullable = false)
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Column(name = "contact_mobile", nullable = false)
	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	@Column(name = "contact_address", nullable = false)
	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	@Column(length = 2048)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "scheduled_time", length = 19, nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	@Column(name = "payment_time", length = 19)
	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	@Column(name = "fulfillment_time", length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFulfillmentTime() {
		return fulfillmentTime;
	}

	public void setFulfillmentTime(Date fulfillmentTime) {
		this.fulfillmentTime = fulfillmentTime;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public enum Status {
		NEW, PAID, COMPLETED, REFUNDABLE, CLOSED;
	}
}
