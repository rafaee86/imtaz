package com.mz.imtaz.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

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
	private Date updatedDate = new Date();

}
