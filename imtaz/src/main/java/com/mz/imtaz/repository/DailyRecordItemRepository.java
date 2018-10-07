package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mz.imtaz.entity.DailyRecordItem;

public interface DailyRecordItemRepository extends JpaRepository<DailyRecordItem, Integer>{

	@Query("Select a from DailyRecordItem a where a.recordUtility.statusFlag = true ")
	List<DailyRecordItem> findAllActive(Sort sort);
}
