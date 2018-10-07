package com.mz.imtaz.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class RecordUtility implements Serializable {

	@Column(nullable = true, length=1)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean statusFlag = Boolean.TRUE;
	@Column(nullable = true, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer userPkid;
	@Temporal(TemporalType.TIMESTAMP)
    @Column
	private Date createdDate;
	@Temporal(TemporalType.TIMESTAMP)
    @Column
	private Date updatedDate;
	@Temporal(TemporalType.TIMESTAMP)
    @Column
	private Date deletedDate;
	public RecordUtility() {
		this.statusFlag = true;
		this.createdDate = new Date();
	}

	public RecordUtility(Integer userPkid) {
		this.userPkid = userPkid;
		this.statusFlag = true;
		this.createdDate = new Date();
	}
	
	public void disabled() {
		this.statusFlag = false;
		this.deletedDate = new Date();
	}

}
