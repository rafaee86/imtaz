package com.mz.imtaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.PaymentDescription;

public interface PaymentDescriptionRepository extends JpaRepository<PaymentDescription, Integer> {

	PaymentDescription findByDescription(@Param("description") String description);
}
