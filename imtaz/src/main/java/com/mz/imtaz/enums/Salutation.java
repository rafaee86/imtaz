package com.mz.imtaz.enums;

import lombok.Getter;

@Getter
public enum Salutation {

	USTAZ("Ustaz"), USTAZAH("Ustazah");
	
	private String description;
	
	private Salutation(String description) {
		this.description = description;
	}
}
