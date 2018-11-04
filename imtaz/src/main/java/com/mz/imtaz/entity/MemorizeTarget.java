package com.mz.imtaz.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name="MEMORIZE_TARGET")
public class MemorizeTarget implements Serializable{

	@Id
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer records;
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

	public MemorizeTarget(Integer records) {
		this.records = records;
	}
}
