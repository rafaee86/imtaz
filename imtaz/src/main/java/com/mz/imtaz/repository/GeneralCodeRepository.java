package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.GeneralCode;

public interface GeneralCodeRepository  extends JpaRepository<GeneralCode, Integer>{

	List<GeneralCode> findByCategoryOrderByLevelAsc(@Param("category") String category);

}
