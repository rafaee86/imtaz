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

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="MEMORIZE_TARGET")
public class MemorizeTarget {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@JoinColumn(name = "RECORDS", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.EAGER)
	@Type(type = "org.hibernate.type.IntegerType")
	private Records records;
	private Integer year;
	private Integer month;
	@Column(nullable = false, length=50)
	@Setter(value = AccessLevel.NONE)
	private String dailyTarget;
	@Column(nullable = false, length=50)
	@Setter(value = AccessLevel.NONE)
	private String startPage;
	@Column(nullable = false, length=50)
	@Setter(value = AccessLevel.NONE)
	private String lastPage;
	@Column(nullable = false, length=50)
	@Setter(value = AccessLevel.NONE)
	private String totalDailyTarget;
	@Column(nullable = false, length=50)
	@Setter(value = AccessLevel.NONE)
	private String totalMemorize;
	@Column(nullable = false, length=50)
	@Setter(value = AccessLevel.NONE)
	private String totalBalance;
	private RecordUtility recordUtility;

	public void setDailyTarget(String dailyTarget) {
		this.dailyTarget = dailyTarget != null ? WordUtils.capitalizeFully(dailyTarget) : null;
	}
	public void setStartPage(String startPage) {
		this.startPage = startPage != null ? WordUtils.capitalizeFully(startPage) : null;
	}
	public void setLastPage(String lastPage) {
		this.lastPage = lastPage != null ? WordUtils.capitalizeFully(lastPage) : null;
	}
	public void setTotalDailyTarget(String totalDailyTarget) {
		this.totalDailyTarget = totalDailyTarget != null ? WordUtils.capitalizeFully(totalDailyTarget) : null;
	}
	public void setTotalMemorize(String totalMemorize) {
		this.totalMemorize = totalMemorize != null ? WordUtils.capitalizeFully(totalMemorize) : null;
	}
	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance != null ? WordUtils.capitalizeFully(totalBalance) : null;
	}
}
