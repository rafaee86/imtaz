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
@Table(name="DAILY_ACTIVITY_ITEM")
public class DailyActivityItem implements Serializable {

	@Id
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer dailyActivity;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer dailyRecordItem;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean done;
}
