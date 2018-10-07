package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mz.imtaz.entity.Bank;

public interface BankRepository extends JpaRepository<Bank, Integer> {

	@Query("Select a from Bank a where a.recordUtility.statusFlag = true ")
	List<Bank> findAllActive(Sort sort);
}
