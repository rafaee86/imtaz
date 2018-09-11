package com.mz.imtaz.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="STUDENT")
public class Student implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@JoinColumn(name = "CLASS_ROOM_DETAIL", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.LAZY)
	@Type(type = "org.hibernate.type.IntegerType")
	private ClassRoomDetail classRoomDetail;
	@Setter(value = AccessLevel.NONE)
	private String name;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer icNo;
	@Setter(value = AccessLevel.NONE)
	private String fatherName;
	@Type(type = "org.hibernate.type.IntegerType")
	private String fatherIcNo;
	@Setter(value = AccessLevel.NONE)
	private String motherName;
	private Integer motherIcNo;
	@Column(nullable = true, length=255)
	@Setter(value = AccessLevel.NONE)
	private String address1;
	@Column(nullable = true, length=255)
	@Setter(value = AccessLevel.NONE)
	private String address2;
	@Column(nullable = true, length=5)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer poscode;
	@Column(nullable = true, length=50)
	@Setter(value = AccessLevel.NONE)
	private String state;
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	@Column(nullable = false, length=2)
	private Integer posInSibling;
	@Column(nullable = false, length=1)
	@Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isStudyOtherSchool = Boolean.FALSE;
	@Column(nullable = true, length=250)
	@Setter(value = AccessLevel.NONE)
	private String nameOtherSchool;
	@Column(nullable = true, length=250)
	@Setter(value = AccessLevel.NONE)
	private String causeQuitOtherSchool;
	@Column(nullable = false, length=250)
	@Setter(value = AccessLevel.NONE)
	private String memorizationDesc;
	@Column(nullable = false, length=250)
	@Setter(value = AccessLevel.NONE)
	private String registerJustification;
	@Embedded
	private RecordUtility recordInfo;

	public void setName(String name) {
		this.name = name != null ? WordUtils.capitalizeFully(name) : null;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName != null ? WordUtils.capitalizeFully(fatherName) : null;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName != null ? WordUtils.capitalizeFully(motherName) : null;
	}
	public void setAddress1(String address1) {
		this.address1 = address1 != null ? WordUtils.capitalizeFully(address1) : null;
	}
	public void setAddress2(String address2) {
		this.address2 = address2 != null ? WordUtils.capitalizeFully(address2) : null;
	}
	public void setState(String state) {
		this.state = state != null ? WordUtils.capitalizeFully(state) : null;
	}
	public void setNameOtherSchool(String nameOtherSchool) {
		this.nameOtherSchool = nameOtherSchool != null ? WordUtils.capitalizeFully(nameOtherSchool) : null;
	}
	public void setCauseQuitOtherSchool(String causeQuitOtherSchool) {
		this.causeQuitOtherSchool = causeQuitOtherSchool != null ? WordUtils.capitalizeFully(causeQuitOtherSchool) : null;
	}
	public void setMemorizationDesc(String memorizationDesc) {
		this.memorizationDesc = memorizationDesc != null ? WordUtils.capitalizeFully(memorizationDesc) : null;
	}
	public void setRegisterJustification(String registerJustification) {
		this.registerJustification = registerJustification != null ? WordUtils.capitalizeFully(registerJustification) : null;
	}
}
