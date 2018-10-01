package com.mz.imtaz.enums;

public enum PaymentType {

	CASH("Tunai"), BANK_TRANSFER("Pindahan Bank"), CHEQUE("Cheque");

	private String description;

	PaymentType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
