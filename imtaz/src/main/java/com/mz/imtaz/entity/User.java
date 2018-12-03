package com.mz.imtaz.entity;

import java.io.Serializable;
import java.time.LocalDate;

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
@Setter
@Getter
@Entity
@Table(name="USER")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Column(nullable = false, length=50)
	private String username;
	@Column(nullable = false, length=250)
	private String password;
	@Column(nullable = false, length=50)
	private String plainPassword;
	@Setter(value = AccessLevel.NONE)
	@Column(nullable = false, length=200)
	private String fullname;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean enabled;	
	private LocalDate expiredDate;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isLock;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isAdministrator;
	@Embedded
	private RecordUtility recordUtility;
	
	public void setFullname(String fullname) {
		this.fullname = fullname != null ? WordUtils.capitalizeFully(fullname) : null;
	}
}
