package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.MemorizeTarget;
import com.mz.imtaz.entity.Student;

public interface MemorizeTargetRepository extends JpaRepository<MemorizeTarget, Integer>{

	@Query("Select a from MemorizeTarget a where a.recordUtility.statusFlag = true and a.records.classRoomDetail = :classRoomDetail and a.records.student = :student order by a.year, a.month desc")
	List<MemorizeTarget> findByClassRoomDetail(@Param("classRoomDetail") ClassRoomDetail classRoomDetail, @Param("student") Student student);

	@Query("Select a from MemorizeTarget a where a.recordUtility.statusFlag = true and a.records.classRoomDetail = :classRoomDetail and a.records.student = :student order by a.year, a.month desc")
	List<MemorizeTarget> findByClassRoomDetailPageable(@Param("classRoomDetail") ClassRoomDetail classRoomDetail, @Param("student") Student student, Pageable pageable);
}
