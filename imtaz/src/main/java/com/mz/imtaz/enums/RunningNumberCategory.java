package com.mz.imtaz.enums;

public enum RunningNumberCategory {

	STUDENT("S"), PAYMENT("P");
	
	private String code;
	
	private RunningNumberCategory(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
