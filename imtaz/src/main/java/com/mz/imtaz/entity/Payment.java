package com.mz.imtaz.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import com.mz.imtaz.enums.PaymentType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="PAYMENT")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@JoinColumn(name = "Records", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.LAZY)
	@Type(type = "org.hibernate.type.IntegerType")
	private Records records;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date transactionDate;
	private Integer year;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "payment")	
	private List<PaymentMonth> paymentMonth;
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount;
	@Column(nullable = false)
	private PaymentType paymentType;
	@JoinColumn(name = "Bank", referencedColumnName = "PKID")
	@OneToOne(fetch = FetchType.LAZY)
	@Type(type = "org.hibernate.type.IntegerType")
	private Bank bank;
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
