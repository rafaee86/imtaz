package com.mz.imtaz.enums;

public enum ReportFileType {

	PDF("pdf","Pdf");
	
	private String mimeType;
	private String description;
	
	ReportFileType(String mimeType, String description) {
		this.mimeType = mimeType;
		this.description = description;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	public String getDescription() {
		return description;
	}
}
