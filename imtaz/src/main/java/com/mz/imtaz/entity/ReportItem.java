package com.mz.imtaz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Type;

import com.mz.imtaz.enums.ReportFileType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="REPORT_ITEM")
public class ReportItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@JoinColumn(name = "REPORT_HEADER", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.EAGER)
	@Type(type = "org.hibernate.type.IntegerType")
	private ReportHeader reportHeader;
	@Column(nullable = false, length=200)
	@Setter(value = AccessLevel.NONE)
	private String name;
	private String url;
	private ReportFileType reportFileType;
	@Column(nullable = false, length=2)
	private Integer level;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean active;

	public void setName(String name) {
		this.name = name != null ? WordUtils.capitalizeFully(name) : null;
	}
}
