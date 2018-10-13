package com.mz.imtaz.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.mz.imtaz.enums.ReportFileType;

@Converter(autoApply = true)
public class ReportFileTypeConverter  implements AttributeConverter<ReportFileType, String> {

	@Override
	public String convertToDatabaseColumn(ReportFileType attribute) {
		return attribute.name();
	}

	@Override
	public ReportFileType convertToEntityAttribute(String dbData) {
		return dbData != null ? ReportFileType.valueOf(dbData) : null;
	}

}
