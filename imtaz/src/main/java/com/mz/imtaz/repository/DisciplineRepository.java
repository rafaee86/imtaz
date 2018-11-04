package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mz.imtaz.entity.Discipline;

public interface DisciplineRepository extends JpaRepository<Discipline, Integer> {

	@Query("Select a from Discipline a where a.recordUtility.statusFlag = true ")
	List<Discipline> findAllActive(Sort sort);
}
