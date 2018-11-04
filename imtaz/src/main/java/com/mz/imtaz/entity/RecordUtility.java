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

@SuppressWarnings("serial")
@Setter
@Getter
@Embeddable
public class RecordUtility implements Serializable {

	@Column(nullable = false, length=1)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean statusFlag = Boolean.TRUE;
	@Column(nullable = true, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer userAddPkid;
	@Column(nullable = true, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer userUpdatedPkid;
	@Column(nullable = true, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer userDeletePkid;
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

	public RecordUtility(Integer userAddPkid) {
		this.userAddPkid = userAddPkid;
		this.statusFlag = true;
		this.createdDate = new Date();
	}
	
	public void disabled(Integer userDeletePkid) {
		this.statusFlag = false;
		this.userDeletePkid = userDeletePkid;
		this.deletedDate = new Date();
	}

	public void enabled(Integer userUpdatedPkid) {
		this.statusFlag = true;
		edited(userUpdatedPkid);
	}
	
	public void edited(Integer userUpdatedPkid) {
		this.userUpdatedPkid = userUpdatedPkid;
		this.updatedDate = new Date();
	}
}
