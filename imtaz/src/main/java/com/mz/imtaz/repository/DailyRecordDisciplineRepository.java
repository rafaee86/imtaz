package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mz.imtaz.entity.DailyRecordDiscipline;

public interface DailyRecordDisciplineRepository extends JpaRepository<DailyRecordDiscipline, Integer> {

	@Query("Select a from DailyRecordDiscipline a where a.recordUtility.statusFlag = true ")
	List<DailyRecordDiscipline> findAllActive(Sort sort);
}
