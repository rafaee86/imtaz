package com.mz.imtaz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.mz.imtaz.enums.RunningNumberCategory;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="RUNNING_NUMBER")
public class RunningNumber {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
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
