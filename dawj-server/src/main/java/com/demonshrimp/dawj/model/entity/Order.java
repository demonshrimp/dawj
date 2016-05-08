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
	private DiscountRule discountRule;
	private String consultingContent;
	private Integer consultingDuration;
	private Double totalAmount; // 订单总金额
	private Integer discountAmount; // 订单减免金额
	private Double amount; // 订单实付金额
	private String contactName;
	private String contactMobile;
	private String contactAddress;
	private String remark;
	private Date scheduledDate;
	private Date paymentTime;
	private Date fulfillmentTime;
	private Date refundApplyTime;
	private Date closeTime;
	private Status status;

	private String thirdOrderId;
	private PaymentPlatform paymentPlatform;

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

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "discount_rule_id", nullable = false)
	public DiscountRule getDiscountRule() {
		return discountRule;
	}

	public void setDiscountRule(DiscountRule discountRule) {
		this.discountRule = discountRule;
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

	@Column(name = "total_amount", scale = 2, nullable = false)
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "discount_amount")
	public Integer getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Integer discountAmount) {
		this.discountAmount = discountAmount;
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

	@Column(name = "contact_address", nullable = true)
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

	@Column(name = "scheduled_date", length = 10, nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
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

	@Column(name = "refund_apply_time", length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRefundApplyTime() {
		return refundApplyTime;
	}

	public void setRefundApplyTime(Date refundApplyTime) {
		this.refundApplyTime = refundApplyTime;
	}

	@Column(name = "close_time", length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(name = "third_order_id")
	public String getThirdOrderId() {
		return thirdOrderId;
	}

	public void setThirdOrderId(String thirdOrderId) {
		this.thirdOrderId = thirdOrderId;
	}

	@Column(name = "payment_platform")
	@Enumerated(EnumType.ORDINAL)
	public PaymentPlatform getPaymentPlatform() {
		return paymentPlatform;
	}

	public void setPaymentPlatform(PaymentPlatform paymentPlatform) {
		this.paymentPlatform = paymentPlatform;
	}

	public enum Status {
		NEW, PAID, COMPLETED, REFUNDABLE, CLOSED;
	}

	public enum PaymentPlatform {
		ALIPAY, WECHAT;
	}
}
