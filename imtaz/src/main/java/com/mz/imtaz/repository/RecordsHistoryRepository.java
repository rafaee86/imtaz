package com.mz.imtaz.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.RecordsHistory;

public interface RecordsHistoryRepository extends JpaRepository<RecordsHistory, Integer> {

	@Query("Select a from RecordsHistory a where a.studentPkid = :studentPkid order by a.transactionDate desc")
	List<RecordsHistory> findByStudent(@Param("studentPkid") Integer studentPkid);
	
	@Query("Select a from RecordsHistory a where a.studentPkid = :studentPkid order by a.transactionDate desc")
	List<RecordsHistory> findByStudentPageable(@Param("studentPkid") Integer studentPkid, Pageable pageable);
	
	@Query("Select a from RecordsHistory a where a.studentPkid = :studentPkid and a.transactionDate between :dateFrom and :dateTo order by a.transactionDate desc")
	List<RecordsHistory> findByStudent(@Param("studentPkid") Integer studentPkid, @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);
	
	@Query("Select a from RecordsHistory a where a.studentPkid = :studentPkid and a.transactionDate between :dateFrom and :dateTo order by a.transactionDate desc")
	List<RecordsHistory> findByStudentPageable(@Param("studentPkid") Integer studentPkid, @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo, Pageable pageable);
	
	
}
