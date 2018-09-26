package com.mz.imtaz.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="DAILY_ACTIVITY_ITEM")
public class DailyActivityItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@JoinColumn(name = "DailyActivity", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.EAGER)
	@Type(type = "org.hibernate.type.IntegerType")
	private DailyActivity dailyActivity;
	@JoinColumn(name = "DailyRecordItem", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.EAGER)
	@Type(type = "org.hibernate.type.IntegerType")
	private DailyRecordItem dailyRecordItem;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean done;
	@Column(nullable = false, length=50)
	@Setter(value = AccessLevel.NONE)
	private String remarks;
	@JoinColumn(name = "DICIPLINE", referencedColumnName = "PKID")
	@OneToMany(fetch = FetchType.EAGER)
	private List<Dicipline> diciplineIssues;

	public void setRemarks(String remarks) {
		this.remarks = remarks != null ? WordUtils.capitalizeFully(remarks) : null;
	}
}
