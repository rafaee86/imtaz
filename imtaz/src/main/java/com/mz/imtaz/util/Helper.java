package com.mz.imtaz.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Helper {

	public static Date localDateToDate(LocalDate localDate) {

		Date result = null;
		if(localDate != null) {
			result = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		}
		return result;
	}
}
