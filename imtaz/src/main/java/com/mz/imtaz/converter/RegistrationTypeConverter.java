package com.mz.imtaz.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.mz.imtaz.enums.RegistrationType;

@Converter(autoApply = true)
public class RegistrationTypeConverter implements AttributeConverter<RegistrationType, String>{

	@Override
	public String convertToDatabaseColumn(RegistrationType attribute) {
		return attribute.name();
	}

	@Override
	public RegistrationType convertToEntityAttribute(String dbData) {
		return RegistrationType.valueOf(dbData);
	}

}
