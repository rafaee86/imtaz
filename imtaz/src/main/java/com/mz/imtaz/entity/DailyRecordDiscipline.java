package com.mz.imtaz.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="DAILY_RECORD_DISCIPLINE")
public class DailyRecordDiscipline implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Column(nullable = false, length=50)
	@Setter(value = AccessLevel.NONE)
	private String code;
	@Column(nullable = false, length=200)
	@Setter(value = AccessLevel.NONE)
	private String description;
	@Embedded
	private RecordUtility recordUtility;

	public void setDescription(String description) {
		this.description = description != null ? WordUtils.capitalizeFully(description) : null;
	}
	
	public void setCode(String code) {
		this.code = code != null ? code.toUpperCase() : null;
	}
}
