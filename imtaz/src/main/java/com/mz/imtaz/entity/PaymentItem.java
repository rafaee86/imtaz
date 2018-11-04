package com.mz.imtaz.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="PAYMENT_ITEM")
public class PaymentItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@JoinColumn(name = "Payment", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.LAZY)
	@Type(type = "org.hibernate.type.IntegerType")
	private Payment payment;
	@JoinColumn(name = "PaymentDescription", referencedColumnName = "PKID")
	@OneToOne(fetch = FetchType.LAZY)
	@Type(type = "org.hibernate.type.IntegerType")
	private PaymentDescription description;
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	public PaymentItem() {}

	public PaymentItem(Payment payment) {
		this.payment = payment;
	}
	
	public Double getAmountDouble() {
		return this.amount != null ? this.amount.doubleValue() : null;
	}
}
