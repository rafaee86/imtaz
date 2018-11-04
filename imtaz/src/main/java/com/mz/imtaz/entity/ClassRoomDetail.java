package com.mz.imtaz.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
@Table(name="CLASS_ROOM_DETAIL")
public class ClassRoomDetail implements Serializable {

	@Id
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer classRoom;
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer teacher;
	@Column(nullable = false, length=50)
	@Setter(value = AccessLevel.NONE)
	private String name;
	@Embedded
	private RecordUtility recordUtility;

	public void setName(String name) {
		this.name = name != null ? WordUtils.capitalizeFully(name) : null;
	}
}
