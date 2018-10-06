package com.mz.imtaz.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.mz.imtaz.enums.DiscipLineStatus;

@Converter(autoApply = true)
public class DiscipLineStatusConverter implements AttributeConverter<DiscipLineStatus, String> {

	@Override
	public String convertToDatabaseColumn(DiscipLineStatus attribute) {
		return attribute != null ? attribute.name() : null;
	}

	@Override
	public DiscipLineStatus convertToEntityAttribute(String dbData) {
		return dbData != null ? DiscipLineStatus.valueOf(dbData) : null;
	}

	
}
