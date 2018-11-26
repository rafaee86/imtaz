package com.mz.imtaz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="DAILY_ACTIVITY_ITEM")
public class DailyActivityItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@JoinColumn(name = "DailyActivity", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.EAGER)
	@Type(type = "org.hibernate.type.IntegerType")
	private DailyActivity dailyActivity;
	@JoinColumn(name = "DailyRecordItem", referencedColumnName = "PKID")
	@OneToOne(fetch = FetchType.EAGER)
	@Type(type = "org.hibernate.type.IntegerType")
	private DailyRecordItem dailyRecordItem;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean done;
}
