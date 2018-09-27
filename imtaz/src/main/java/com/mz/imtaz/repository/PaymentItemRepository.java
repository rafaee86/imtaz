package com.mz.imtaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mz.imtaz.entity.PaymentItem;

public interface PaymentItemRepository extends JpaRepository<PaymentItem, Integer> {

}
