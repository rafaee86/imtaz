package com.mz.imtaz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="DICIPLINE_RECORD_ITEM")
public class DiciplineRecordItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@JoinColumn(name = "DiciplineRecord", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.EAGER)
	@Type(type = "org.hibernate.type.IntegerType")
	private DiciplineRecord diciplineRecord;
	@JoinColumn(name = "Dicipline", referencedColumnName = "PKID")
	@OneToOne(fetch = FetchType.EAGER)
	@Type(type = "org.hibernate.type.IntegerType")
	private Dicipline dicipline;
	
}
