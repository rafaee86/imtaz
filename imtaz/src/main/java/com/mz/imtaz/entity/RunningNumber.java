package com.mz.imtaz.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.mz.imtaz.enums.RunningNumberCategory;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name="RUNNING_NUMBER")
public class RunningNumber implements Serializable {

	@Id
	@Column(nullable = false, length=8)
	@Type(type = "org.hibernate.type.IntegerType")
	private Integer pkid;
	@Column(nullable = false, length=50)
	private RunningNumberCategory category;
	@Column(nullable = false, length=8)
	private Integer running;

	public RunningNumber() {}

	public RunningNumber(RunningNumberCategory category, Integer running) {
		this.category = category;
		this.running = running;
	}

	public void incrementOne() {
		if(this.running != null) {
			this.running ++;
		}
	}
}
