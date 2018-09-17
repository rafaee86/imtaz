package com.mz.imtaz.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Type;

import com.mz.imtaz.enums.GuardianType;
import com.mz.imtaz.enums.RegistrationType;

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
	private RegistrationType registrationType;
	@Setter(value = AccessLevel.NONE)
	@Column(nullable = false, length=250)
	private String name;
	private String icNo;
	@Setter(value = AccessLevel.NONE)
	private String cbNo;
	private LocalDate dob;
	private GuardianType guardianType;
	@Column(nullable = false, length=250)
	private String fatherName;
	@Column(nullable = false, length=12)
	private String fatherIcNo;
	@Column(nullable = false, length=250)
	@Setter(value = AccessLevel.NONE)
	private String fatherJob;
	@Column(nullable = false, length=250)
	@Setter(value = AccessLevel.NONE)
	private String motherName;
	@Column(nullable = false, length=12)
	private String motherIcNo;
	@Column(nullable = false, length=250)
	@Setter(value = AccessLevel.NONE)
	private String motherJob;
	@Column(nullable = false, length=20)
	private String parentTelNo;
	@Column(nullable = false, length=20)
	private String parentHpNo;
	@Column(nullable = true, length=255)
	@Setter(value = AccessLevel.NONE)
	private String address1;
	@Column(nullable = true, length=255)
	@Setter(value = AccessLevel.NONE)
	private String address2;
	@Column(nullable = true, length=5)
	private String poscode;
	@Column(nullable = true, length=50)
	@Setter(value = AccessLevel.NONE)
	private String town;
	@Column(nullable = true, length=50)
	@Setter(value = AccessLevel.NONE)
	private String state;
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	@Setter(value = AccessLevel.NONE)
	@Column(nullable = true, length=250)
	private String sickness;
	@Setter(value = AccessLevel.NONE)
	@Column(nullable = true, length=250)
	private String allergies;
	@Column(nullable = true, length=250)
	@Setter(value = AccessLevel.NONE)
	private String namePrevSchool;
	@Column(nullable = false, length=20)
	private String telNoPrevSchool;
	@Column(nullable = false, length=250)
	@Setter(value = AccessLevel.NONE)
	private String readingQuranDesc;
	@Column(nullable = false, length=250)
	@Setter(value = AccessLevel.NONE)
	private String memorizeQuranDesc;
	@Column(nullable = false, length=250)
	@Setter(value = AccessLevel.NONE)
	private String registerJustification;
	@Column(nullable = false, length=250)
	@Setter(value = AccessLevel.NONE)
	private String registerJustification2;
	@Embedded
	private RecordUtility recordInfo;

	public void setName(String name) {
		this.name = name != null ? WordUtils.capitalizeFully(name) : null;
	}
	public void setCbNo(String cbNo) {
		this.cbNo = cbNo != null ? cbNo.toUpperCase() : null;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName != null ? WordUtils.capitalizeFully(fatherName) : null;
	}
	public void setFatherJob(String fatherJob) {
		this.fatherJob = fatherJob != null ? WordUtils.capitalizeFully(fatherJob) : null;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName != null ? WordUtils.capitalizeFully(motherName) : null;
	}
	public void setMotherJob(String motherJob) {
		this.motherJob = motherJob != null ? WordUtils.capitalizeFully(motherJob) : null;
	}
	public void setAddress1(String address1) {
		this.address1 = address1 != null ? WordUtils.capitalizeFully(address1) : null;
	}
	public void setAddress2(String address2) {
		this.address2 = address2 != null ? WordUtils.capitalizeFully(address2) : null;
	}
	public void setTown(String town) {
		this.town = town != null ? WordUtils.capitalizeFully(town) : null;
	}
	public void setState(String state) {
		this.state = state != null ? WordUtils.capitalizeFully(state) : null;
	}
	public void setSickness(String sickness) {
		this.sickness = sickness != null ? WordUtils.capitalizeFully(sickness) : null;
	}
	public void setAllergies(String allergies) {
		this.allergies = allergies != null ? WordUtils.capitalizeFully(allergies) : null;
	}
	public void setNamePrevSchool(String namePrevSchool) {
		this.namePrevSchool = namePrevSchool != null ? WordUtils.capitalizeFully(namePrevSchool) : null;
	}
	public void setReadingQuranDesc(String readingQuranDesc) {
		this.readingQuranDesc = readingQuranDesc != null ? WordUtils.capitalizeFully(readingQuranDesc) : null;
	}
	public void setMemorizeQuranDesc(String memorizeQuranDesc) {
		this.memorizeQuranDesc = memorizeQuranDesc != null ? WordUtils.capitalizeFully(memorizeQuranDesc) : null;
	}
	public void setRegisterJustification(String registerJustification) {
		this.registerJustification = registerJustification != null ? WordUtils.capitalizeFully(registerJustification) : null;
	}
	public void setRegisterJustification2(String registerJustification2) {
		this.registerJustification2 = registerJustification2 != null ? WordUtils.capitalizeFully(registerJustification2) : null;
	}
}
