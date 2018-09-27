package com.mz.imtaz.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.mz.imtaz.enums.PaymentType;

@Converter(autoApply = true)
public class PaymentTypeConverter implements AttributeConverter<PaymentType, String>{

	@Override
	public String convertToDatabaseColumn(PaymentType attribute) {
		return attribute.name();
	}

	@Override
	public PaymentType convertToEntityAttribute(String dbData) {
		return PaymentType.valueOf(dbData);
	}

}
