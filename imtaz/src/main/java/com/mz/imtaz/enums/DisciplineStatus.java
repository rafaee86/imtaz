package com.mz.imtaz.enums;

public enum DisciplineStatus {

	SUSPENDED("Di Gantung"), WARNING("Amaran"), Expelled("Buang Tahfiz");
	
	private String description;
	
	DisciplineStatus(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
