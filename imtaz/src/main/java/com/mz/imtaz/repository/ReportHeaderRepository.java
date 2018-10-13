package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.ReportHeader;

public interface ReportHeaderRepository extends JpaRepository<ReportHeader, Integer> {
	
	List<ReportHeader> findByActive(@Param("active") Boolean active, Sort sort);
}
