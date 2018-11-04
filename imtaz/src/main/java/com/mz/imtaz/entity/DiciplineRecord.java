package com.mz.imtaz.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Type;

import com.mz.imtaz.enums.DiscipLineStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name="DICIPLINE_RECORD")
public class DiciplineRecord implements Serializable {

	@Id
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer records;
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date offendedDate;
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date actionDate;
	@Setter(value = AccessLevel.NONE)
	private String actionDescription;
	@Setter(value = AccessLevel.NONE)
	private String remarks;
	private DiscipLineStatus status;
	@Embedded
	private RecordUtility recordUtility;
	
	public DiciplineRecord() {}
	
	public DiciplineRecord(Integer records) {
		this.records = records;
	}
	
	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription != null ? WordUtils.capitalizeFully(actionDescription) : null;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks != null ? WordUtils.capitalizeFully(remarks) : null;
	}
}
