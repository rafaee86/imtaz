package com.mz.imtaz.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.mz.imtaz.enums.MiscEntityCategory;

@Converter(autoApply = true)
public class MiscEntityConverter implements AttributeConverter<MiscEntityCategory, String>  {

	@Override
	public String convertToDatabaseColumn(MiscEntityCategory attribute) {
		return attribute.name();
	}

	@Override
	public MiscEntityCategory convertToEntityAttribute(String dbData) {
		return dbData != null ? MiscEntityCategory.valueOf(dbData) : null;
	}

}
