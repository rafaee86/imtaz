package com.mz.imtaz.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer payment;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer description;
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	public PaymentItem() {}

	public PaymentItem(Integer payment) {
		this.payment = payment;
	}
	
	public Double getAmountDouble() {
		return this.amount != null ? this.amount.doubleValue() : null;
	}
}
