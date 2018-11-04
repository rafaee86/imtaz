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
@Table(name="RECORDS")
public class Records implements Cloneable,Serializable{

	@Id
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer student;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer classRoomDetail;
	private RecordUtility recordUtility;

	public Records(){}

	public Records(Integer classRoomDetail) {
		this.classRoomDetail = classRoomDetail;
	}
	
	public Records clone() throws CloneNotSupportedException {
		return (Records) super.clone();
	}

}
