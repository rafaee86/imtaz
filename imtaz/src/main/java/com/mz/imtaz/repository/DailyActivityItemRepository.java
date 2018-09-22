package com.mz.imtaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mz.imtaz.entity.DailyActivityItem;

public interface DailyActivityItemRepository extends JpaRepository<DailyActivityItem, Integer> {

}
