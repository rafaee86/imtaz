package com.mz.imtaz.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.mz.imtaz.enums.GuardianType;

@Converter(autoApply = true)
public class GuardianTypeConverter  implements AttributeConverter<GuardianType, String>{

	@Override
	public String convertToDatabaseColumn(GuardianType attribute) {
		return attribute.name();
	}

	@Override
	public GuardianType convertToEntityAttribute(String dbData) {
		return GuardianType.valueOf(dbData);
	}

}
