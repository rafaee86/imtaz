package com.mz.imtaz.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.CashFlow;

public interface CashFlowRepository extends JpaRepository<CashFlow, Integer> {

	List<CashFlow> findByTransactionDate(@Param("date") Date date, Sort sort);
}
