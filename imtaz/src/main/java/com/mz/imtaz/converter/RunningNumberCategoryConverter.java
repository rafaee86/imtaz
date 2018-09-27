package com.mz.imtaz.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.mz.imtaz.enums.RunningNumberCategory;

@Converter(autoApply = true)
public class RunningNumberCategoryConverter implements AttributeConverter<RunningNumberCategory, String>{

	@Override
	public String convertToDatabaseColumn(RunningNumberCategory attribute) {
		return attribute.name();
	}

	@Override
	public RunningNumberCategory convertToEntityAttribute(String dbData) {
		return RunningNumberCategory.valueOf(dbData);
	}

}
