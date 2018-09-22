package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.Records;

public interface RecordsRepository extends JpaRepository<Records, Integer> {

	@Query("Select a from Records a where a.classRoomDetail = :classRoomDetail order by a.student.name asc")
	List<Records> findRecordsByClassRoomDetail(@Param("classRoomDetail") ClassRoomDetail classRoomDetail);

	@Query("Select a from Records a where a.classRoomDetail = :classRoomDetail order by a.student.name asc")
	List<Records> findRecordsByClassRoomDetailPageable(@Param("classRoomDetail") ClassRoomDetail classRoomDetail, Pageable page);
}
