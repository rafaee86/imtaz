package com.mz.imtaz.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name="DAILY_ACTIVITY")
public class DailyActivity implements Serializable {

	@Id
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer records;
	private LocalDate date;
	@Transient
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private String remarks;
	private RecordUtility recordUtility;
	private Integer diciplineIssues;

	public String getRemarks() {
		String remarks = null;
		if(this.diciplineIssues != null && this.diciplineIssues > 10) {
			remarks = "Tidak boleh outing";
		}else {
			remarks = "Boleh outing";
		}
		return remarks;
	}
}
