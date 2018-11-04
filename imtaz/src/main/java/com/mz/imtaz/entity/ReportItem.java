package com.mz.imtaz.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Type;

import com.mz.imtaz.enums.ReportFileType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name="REPORT_ITEM")
public class ReportItem implements Serializable {

	@Id
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer reportHeader;
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
