package com.mz.imtaz.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="RECORDS_HISTORY")
public class RecordsHistory implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Column(nullable = false, length=8)
	private Integer studentPkid;	
	@Column(nullable = true, length=200)
	private String classRoomDescription;
	@Temporal(TemporalType.TIMESTAMP)
	private Date transactionDate;
	@Setter(value = AccessLevel.NONE)
	@Column(nullable = true, length=200)
	private String description;
	private Integer transactionPkid;
	private String operatorName;
	
	public void setDescription(String description) {
		this.description = description != null ? WordUtils.capitalizeFully(description) : null;
	}
}
