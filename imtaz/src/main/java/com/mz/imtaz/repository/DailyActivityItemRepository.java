package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.DailyActivity;
import com.mz.imtaz.entity.DailyActivityItem;

public interface DailyActivityItemRepository extends JpaRepository<DailyActivityItem, Integer> {

	List<DailyActivityItem> findByDailyActivity(@Param("dailyActivity") DailyActivity dailyActivity);
	
}
