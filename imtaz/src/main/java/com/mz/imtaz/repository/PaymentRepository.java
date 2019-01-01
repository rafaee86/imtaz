package com.mz.imtaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mz.imtaz.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

	@Query(value = "SELECT LPAD( " + 
			"	CONVERT(" + 
			"		RIGHT(IFNULL((SELECT MAX(TRANSACTION_Id) FROM PAYMENT),'0'),5), " + 
			"	 UNSIGNED INTEGER " + 
			"	) " + 
			"+1,5,'0') FROM DUAL", nativeQuery = true)
	String getPaymentNoMax();
}
