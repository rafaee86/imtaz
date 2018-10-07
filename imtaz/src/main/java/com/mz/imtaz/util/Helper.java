package com.mz.imtaz.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.mz.imtaz.entity.RecordUtility;
import com.mz.imtaz.entity.RecordsHistory;
import com.mz.imtaz.repository.RecordsHistoryRepository;

public class Helper {
	
	private static final String DATE_FORMAT_DEFAULT = "dd/MM/yyyy";

	public static Date localDateToDate(LocalDate localDate) {

		Date result = null;
		if(localDate != null) {
			result = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		}
		return result;
	}
	
	public static String dateFormat(Date date, String format) {

		String result = null;
		if(date != null && format != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			result = sdf.format(date);
		}
		return result;
	}
	
	public static String dateFormat(Date date) {
		return dateFormat(date, DATE_FORMAT_DEFAULT);
	}
	
	public static <T> T notNull(T t) {
		try {
			return t;
		}catch (Exception e) {
			return null;
		}
	}
	
	public static void setRecordsHistory(RecordsHistoryRepository repo, String description, Integer studentPkid) {
		if(repo != null) {
			RecordsHistory recordsHistory = new RecordsHistory();
			recordsHistory.setTransactionDate(new Date());
			recordsHistory.setDescription(description);
			recordsHistory.setStudentPkid(studentPkid);
			repo.save(recordsHistory);
		}
	}
}
