package com.mz.imtaz.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.mz.imtaz.enums.DisciplineStatus;

@Converter(autoApply = true)
public class DisciplineStatusConverter implements AttributeConverter<DisciplineStatus, String> {

	@Override
	public String convertToDatabaseColumn(DisciplineStatus attribute) {
		return attribute != null ? attribute.name() : null;
	}

	@Override
	public DisciplineStatus convertToEntityAttribute(String dbData) {
		return dbData != null ? DisciplineStatus.valueOf(dbData) : null;
	}

	
}
