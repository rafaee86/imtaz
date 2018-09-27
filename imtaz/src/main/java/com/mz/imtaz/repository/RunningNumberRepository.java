package com.mz.imtaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.RunningNumber;
import com.mz.imtaz.enums.RunningNumberCategory;

public interface RunningNumberRepository extends JpaRepository<RunningNumber, Integer>{

	RunningNumber findByCategory(@Param("category") RunningNumberCategory category);
}
