package com.mz.imtaz.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.mz.imtaz.entity.Juzuk;

@Converter(autoApply = true)
public class JuzukConverter  implements AttributeConverter<Juzuk, String>{

	@Override
	public String convertToDatabaseColumn(Juzuk attribute) {
		String result = "";
		if(attribute != null) {
			result += (attribute.getJuz() != null ? attribute.getJuz() : "0") + "/";
			result += (attribute.getPage() != null ? attribute.getPage() : "0") + "/";
			result += (attribute.getLine() != null ? attribute.getLine() : "0");
		}

		return result;
	}

	@Override
	public Juzuk convertToEntityAttribute(String dbData) {
		Juzuk juzuk = new Juzuk();
		if(dbData != null && !dbData.isEmpty()) {
			String[] arr = dbData.split("/");
			if(arr.length == 3) {
				juzuk.setJuz(new Integer(arr[0]));
				juzuk.setPage(new Integer(arr[1]));
				juzuk.setLine(new Integer(arr[2]));
			}
		}

		return juzuk;
	}

}
