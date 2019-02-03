package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.Records;
import com.mz.imtaz.entity.Student;

public interface RecordsRepository extends JpaRepository<Records, Integer> {

	@Query("Select a from Records a "
			+ "where a.recordUtility.statusFlag = true "
			+ "and a.pkid = (select max(ir.pkid) from Records ir where ir.student = a.student) "
			+ "and a.classRoomDetail = :classRoomDetail "
			+ "order by a.student.name asc")
	List<Records> findRecordsByClassRoomDetail(@Param("classRoomDetail") ClassRoomDetail classRoomDetail);

	@Query("Select a from Records a "
			+ "where a.recordUtility.statusFlag = true "
			+ "and a.pkid = (select max(ir.pkid) from Records ir where ir.student = a.student) "
			+ "and a.classRoomDetail = :classRoomDetail "
			+ "order by a.student.name asc")
	List<Records> findRecordsByClassRoomDetailPageable(@Param("classRoomDetail") ClassRoomDetail classRoomDetail, Pageable page);

	@Query("Select a from Records "
			+ "a where a.recordUtility.statusFlag = true "
			+ "and a.pkid = (select max(ir.pkid) from Records ir where ir.student = a.student) "
			+ "and a.classRoomDetail = :classRoomDetail "
			+ "and a.student = :student "
			+ "order by a.student.name asc")
	Records findRecordsByClassRoomDetailAndStudent(@Param("classRoomDetail") ClassRoomDetail classRoomDetail, @Param("student") Student student);
}
