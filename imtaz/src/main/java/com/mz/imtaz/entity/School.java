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
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name="SCHOOL")
public class School implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Column(nullable = false, length=200)
	@Setter(value = AccessLevel.NONE)
	private String name;
	@Column(nullable = false, length=50)
	@Setter(value = AccessLevel.NONE)
	private String shortName;
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
	private String town;
	@Column(nullable = true, length=50)
	@Setter(value = AccessLevel.NONE)
	private String state;
	@Embedded
	private RecordUtility recordUtility;
	
	public void setName(String name) {
		this.name = name != null ? WordUtils.capitalizeFully(name) : null;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName != null ? WordUtils.capitalizeFully(shortName) : null;
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

}
