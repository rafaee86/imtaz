package com.mz.imtaz.dto;

import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.School;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SchoolDTO implements MainDTO<School>{

	private Integer pkid;
	private String name;
	private String shortName;
	private String address1;
	private String address2;
	private Integer poscode;
	private String town;
	private String state;
	private RecordUtility recordUtility;
	
	SchoolDTO(){}
	
	@Override
	public void pull(School school) {
		if(school != null) {
			this.pkid = school.getPkid();
			this.name = school.getName();
			this.shortName = school.getShortName();
			this.address1 = school.getAddress1();
			this.address2 = school.getAddress2();
			this.poscode = school.getPoscode();
			this.town = school.getTown();
			this.state = school.getState();
			this.recordUtility = school.getRecordUtility();
		}
	}
	
	@Override
	public School push(School school) {
		if(school == null)
			school = new School();
		
		school.setPkid(this.getPkid());
		school.setName(this.getName());
		school.setShortName(this.getShortName());
		school.setAddress1(this.getAddress1());
		school.setAddress2(this.getAddress2());
		school.setPoscode(this.getPoscode());
		school.setTown(this.getTown());
		school.setState(this.getState());
		
		if(school.getRecordUtility() != null) {
			school.setRecordUtility(new RecordUtility());
		};
		
		return school;
	}
	
	
}
