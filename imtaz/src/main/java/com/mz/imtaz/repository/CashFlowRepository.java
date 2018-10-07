package com.mz.imtaz.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.CashFlow;

public interface CashFlowRepository extends JpaRepository<CashFlow, Integer> {

	@Query("Select a from CashFlow a where a.recordUtility.statusFlag = true and a.transactionDate = :date ")
	List<CashFlow> findByTransactionDate(@Param("date") Date date, Sort sort);
}
