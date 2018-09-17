package com.mz.imtaz.enums;

public enum GuardianType {
	BIOLOGICAL_PARENT("Ibu Bapa Kandung"), GUARDIAN("Penjaga");

	private String description;

	private GuardianType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
