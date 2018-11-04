package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.DisciplineRecord;
import com.mz.imtaz.entity.Student;

public interface DisciplineRecordRepository extends JpaRepository<DisciplineRecord, Integer> {
	
	@Query("Select a from DisciplineRecord a where a.recordUtility.statusFlag = true and a.records.classRoomDetail = :classRoomDetail and a.records.student = :student order by a.records.student.name asc")
	List<DisciplineRecord> findByClassRoomDetail(@Param("classRoomDetail") ClassRoomDetail classRoomDetail, @Param("student") Student student);
	
	@Query("Select a from DisciplineRecord a where a.recordUtility.statusFlag = true and a.records.classRoomDetail = :classRoomDetail and a.records.student = :student order by a.records.student.name asc")
	List<DisciplineRecord> findByClassRoomDetailPageable(@Param("classRoomDetail") ClassRoomDetail classRoomDetail, @Param("student") Student student, Pageable pageable);

}
