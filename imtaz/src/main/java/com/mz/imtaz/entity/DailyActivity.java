package com.mz.imtaz.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="DAILY_ACTIVITY")
public class DailyActivity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@JoinColumn(name = "RECORDS", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.LAZY)
	@Type(type = "org.hibernate.type.IntegerType")
	private Records records;
	private LocalDate date;
	@Transient
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private String remarks;
	private RecordUtility recordUtility;
	private Integer disciplineIssues;

	public String getRemarks() {
		String remarks = null;
		if(this.disciplineIssues != null && this.disciplineIssues > 10) {
			remarks = "Tidak boleh outing";
		}else {
			remarks = "Boleh outing";
		}
		return remarks;
	}
}
