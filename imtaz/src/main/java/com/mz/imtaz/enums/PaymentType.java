package com.mz.imtaz.enums;

public enum PaymentType {

	CASH("Tunai"), CREDIT_CARD("Kad Kredit"), BANK_TRANSFER("Pindahan Bank"), CHEQUE("Cheque");

	private String description;

	PaymentType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
