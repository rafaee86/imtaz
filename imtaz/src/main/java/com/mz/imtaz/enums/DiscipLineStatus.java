package com.mz.imtaz.enums;

public enum DiscipLineStatus {

	SUSPENDED("Di Gantung"), WARNING("Amaran"), Expelled("Buang Tahfiz");
	
	private String description;
	
	DiscipLineStatus(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
