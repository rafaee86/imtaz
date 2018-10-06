package com.mz.imtaz.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Type;

import com.mz.imtaz.enums.DiscipLineStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="DICIPLINE_RECORD")
public class DiciplineRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@JoinColumn(name = "Records", referencedColumnName = "PKID")
	@ManyToOne(fetch = FetchType.EAGER)
	@Type(type = "org.hibernate.type.IntegerType")
	private Records records;
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date offendedDate;
	@JoinColumn(name = "DiciplineRecordItem", referencedColumnName = "PKID")
	@OneToMany(fetch = FetchType.EAGER)
	List<DiciplineRecordItem> diciplineRecordItemList;
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date actionDate;
	@Setter(value = AccessLevel.NONE)
	private String actionDescription;
	@Setter(value = AccessLevel.NONE)
	private String remarks;
	private DiscipLineStatus status;
	@Embedded
	private RecordUtility recordUtility;
	
	public DiciplineRecord() {}
	
	public DiciplineRecord(Records records) {
		this.records = records;
	}
	
	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription != null ? WordUtils.capitalizeFully(actionDescription) : null;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks != null ? WordUtils.capitalizeFully(remarks) : null;
	}
	
	public void addDiciplineRecordItemList(DiciplineRecordItem diciplineRecordItem) {
		if(this.diciplineRecordItemList == null)this.diciplineRecordItemList = new ArrayList<DiciplineRecordItem>();
		this.diciplineRecordItemList.add(diciplineRecordItem);
	}
}
