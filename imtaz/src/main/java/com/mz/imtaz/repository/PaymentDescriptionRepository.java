package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.PaymentDescription;

public interface PaymentDescriptionRepository extends JpaRepository<PaymentDescription, Integer> {

	@Query("Select a from PaymentDescription a where a.recordUtility.statusFlag = true ")
	List<PaymentDescription> findAllActive(Sort sort);
	
	@Query("Select a from PaymentDescription a where a.recordUtility.statusFlag = true and a.description = :description")
	PaymentDescription findByDescription(@Param("description") String description);
}
