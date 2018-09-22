package com.mz.imtaz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="DAILY_RECORD_ITEM")
public class DailyRecordItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Column(nullable = false, length=50)
	@Setter(value = AccessLevel.NONE)
	private String description;
	private Integer sequence;
	private RecordUtility recordUtility;

	public void setDescription(String description) {
		this.description = description != null ? WordUtils.capitalizeFully(description) : null;
	}
}
