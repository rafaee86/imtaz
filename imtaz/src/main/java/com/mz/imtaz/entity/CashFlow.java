package com.mz.imtaz.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="CASH_FLOW")
public class CashFlow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Temporal(TemporalType.DATE)
	private Date transactionDate;
	@Setter(value = AccessLevel.NONE)
	@Column(nullable = false, length = 200)
	private String description;
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;
	@Embedded
	private RecordUtility recordUtility;

	public CashFlow() {}

	public CashFlow(BigDecimal amount) {
		this.amount = amount;
	}

	public void setDescription(String description) {
		this.description = description != null ? WordUtils.capitalizeFully(description) : null;
	}
}
