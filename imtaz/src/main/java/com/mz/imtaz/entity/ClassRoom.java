package com.mz.imtaz.entity;

import lombok.Getter;

@Getter
public enum ClassRoom {

	IQRA("Iqra'", 1),
	HAFAZAN("Hafazan", 2),
	KITAB("Kitab", 3);
	
	private String description;
	private int level;
	
	private ClassRoom(String description, int level) {
		this.description = description;
		this.level = level;
	}
	
}
