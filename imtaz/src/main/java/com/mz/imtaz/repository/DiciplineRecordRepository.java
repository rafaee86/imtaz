package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.DiciplineRecord;
import com.mz.imtaz.entity.Student;

public interface DiciplineRecordRepository extends JpaRepository<DiciplineRecord, Integer> {
	
	@Query("Select a from DiciplineRecord a where a.records.classRoomDetail = :classRoomDetail and a.records.student = :student order by a.records.student.name asc")
	List<DiciplineRecord> findByClassRoomDetail(@Param("classRoomDetail") ClassRoomDetail classRoomDetail, @Param("student") Student student);
	
	@Query("Select a from DiciplineRecord a where a.records.classRoomDetail = :classRoomDetail and a.records.student = :student order by a.records.student.name asc")
	List<DiciplineRecord> findByClassRoomDetailPageable(@Param("classRoomDetail") ClassRoomDetail classRoomDetail, @Param("student") Student student, Pageable pageable);

}
