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

import com.mz.imtaz.enums.MiscEntityCategory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="MISC_ENTITY")
public class MiscEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Column(nullable = false, length=50)
	private MiscEntityCategory category;
	@Column(nullable = false, length=200)
	@Setter(value = AccessLevel.NONE)
	private String description;
	@Column(nullable = false, length=50)
	private String value1;
	@Embedded
	private RecordUtility recordUtility;
	
	public MiscEntity(MiscEntityCategory category) {
		this.category = category;
	}

	public void setDescription(String description) {
		this.description = description != null ? WordUtils.capitalizeFully(description) : null;
	}
}
