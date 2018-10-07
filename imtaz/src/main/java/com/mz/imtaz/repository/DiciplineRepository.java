package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mz.imtaz.entity.Dicipline;

public interface DiciplineRepository extends JpaRepository<Dicipline, Integer> {

	@Query("Select a from Dicipline a where a.recordUtility.statusFlag = true ")
	List<Dicipline> findAllActive(Sort sort);
}
