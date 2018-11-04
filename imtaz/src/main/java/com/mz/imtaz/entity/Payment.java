package com.mz.imtaz.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.mz.imtaz.enums.PaymentType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name="PAYMENT")
public class Payment implements Serializable {

	@Id
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer records;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date transactionDate;
	private Integer year;
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount;
	@Column(nullable = false)
	private PaymentType paymentType;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer bank;
	@Setter(value = AccessLevel.NONE)
	@Column(nullable = true, length = 50)
	private String referenceId;
	@Setter(value = AccessLevel.NONE)
	@Column(nullable = false, length = 50)
	private String transactionId;
	@Embedded
	private RecordUtility recordUtility;

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId != null ? referenceId.toUpperCase() : null;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId != null ? transactionId.toUpperCase() : null;
	}
}
