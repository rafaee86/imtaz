package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.GeneralCode;

public interface GeneralCodeRepository  extends JpaRepository<GeneralCode, Integer>{

	@Query("Select a from GeneralCode a where a.recordUtility.statusFlag = true and a.category = :category")
	List<GeneralCode> findByCategory(@Param("category") String category, Sort sort);

}
