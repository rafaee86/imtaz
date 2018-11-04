package com.mz.imtaz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="MEMORIZE_TARGET")
public class MemorizeTarget{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@JoinColumn(name = "RECORDS", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.LAZY)
	@Type(type = "org.hibernate.type.IntegerType")
	private Records records;
	private Integer year;
	private Integer month;
	@Column(nullable = false, length=50)
	private Juzuk dailyTarget;
	@Column(nullable = false, length=50)
	private Integer startPage;
	@Column(nullable = false, length=50)
	private Integer lastPage;
	@Column(nullable = false, length=50)
	private Juzuk totalDailyTarget;
	@Column(nullable = false, length=50)
	private Juzuk totalMemorize;
	@Column(nullable = false, length=50)
	private Juzuk totalBalance;
	private RecordUtility recordUtility;

	public MemorizeTarget() {}

	public MemorizeTarget(Records records) {
		this.records = records;
	}
}
