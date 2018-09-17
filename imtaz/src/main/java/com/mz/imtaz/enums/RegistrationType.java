package com.mz.imtaz.enums;

public enum RegistrationType {

	FULLTIME("Sepenuh Masa"), WEEKLY("Mingguan"), DAILY("Harian");

	private String description;

	private RegistrationType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
