package com.mz.imtaz.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.mz.imtaz.entity.ClassRoom;

@Converter(autoApply = true)
public class ClassRoomConverter implements AttributeConverter<ClassRoom, String>  {

	@Override
	public String convertToDatabaseColumn(ClassRoom attribute) {
		return attribute.name();
	}

	@Override
	public ClassRoom convertToEntityAttribute(String dbData) {
		return dbData != null ? ClassRoom.valueOf(dbData) : null;
	}

}
