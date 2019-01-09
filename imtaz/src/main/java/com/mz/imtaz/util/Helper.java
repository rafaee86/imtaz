package com.mz.imtaz.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import com.mz.imtaz.entity.RecordsHistory;
import com.mz.imtaz.entity.UserContext;
import com.mz.imtaz.repository.RecordsHistoryRepository;
import com.vaadin.data.provider.ListDataProvider;

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
	
	public static <T extends String> T notNull(T t) {
		try {
			return t != null && !t.isEmpty() ? t : null;
		}catch (Exception e) {
			return null;
		}
	}
	
	public static <T> Boolean isNotNull(T t) {
		return notNull(t) != null;
	}
	
	public static <T> T notNull(Optional<T> x) {
		try {
			return x.isPresent() ? x.get() : null;
		}catch (Exception e) {
			return null;
		}
	}
	
	public static void setRecordsHistory(RecordsHistoryRepository repo, String description, Integer studentPkid, String classRoomDescription) {
		if(repo != null) {
			RecordsHistory recordsHistory = new RecordsHistory();
			recordsHistory.setTransactionDate(new Date());
			recordsHistory.setDescription(description);
			recordsHistory.setStudentPkid(studentPkid);
			recordsHistory.setClassRoomDescription(classRoomDescription);
			recordsHistory.setOperatorName(getUserContext().getUsername());
			repo.save(recordsHistory);
		}
	}
	
	public static void setRecordsHistory(RecordsHistoryRepository repo, String description, Integer studentPkid) {
		setRecordsHistory(repo, description, studentPkid, null);
	}
	
	public static String formatBigDecimal(BigDecimal source) {
		String result = null;
		if(source != null) {
			source = source.setScale(2, BigDecimal.ROUND_HALF_UP);
			result = source.toPlainString();
		}else {
			result = "0.00";
		}
		return result;
	}
	
	public static UserContext getUserContext() {
		try {
			Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(obj instanceof UserContext) {
				return (UserContext)obj;
			}else {
				return null;
			}
		}catch (Exception e) {
			return null;
		}
	}
	
	public static <B> void removeGrid(ListDataProvider<B> listDataProvider, B b, boolean isNew) {		
		Objects.requireNonNull(b);		
		if(isNew) {
			listDataProvider.getItems().remove(b);
		}		
		listDataProvider.refreshAll();		
	}
}
