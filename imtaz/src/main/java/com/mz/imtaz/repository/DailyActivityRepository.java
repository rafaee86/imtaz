package com.mz.imtaz.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.DailyActivity;
import com.mz.imtaz.entity.Student;

public interface DailyActivityRepository extends JpaRepository<DailyActivity, Integer> {

	@Query("Select a from DailyActivity a "
			+ "where a.recordUtility.statusFlag = true "
			+ "and a.records.recordUtility.statusFlag = true "
			+ "and a.records.classRoomDetail.recordUtility.statusFlag = true "
			+ "and a.records.classRoomDetail = :classRoomDetail "
			+ "and a.records.student = :student "
			+ "and a.date = :date "
			+ "order by a.records.student.name asc")
	DailyActivity findByClassRoomDetail(@Param("classRoomDetail") ClassRoomDetail classRoomDetail, @Param("student") Student student, @Param(value = "date") LocalDate date);
}
