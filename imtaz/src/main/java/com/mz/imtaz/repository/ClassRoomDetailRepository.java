package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mz.imtaz.entity.ClassRoomDetail;

public interface ClassRoomDetailRepository extends JpaRepository<ClassRoomDetail, Integer> {

	@Query("Select a from ClassRoomDetail a order by a.classRoom.level, a.teacher.name asc")
	List<ClassRoomDetail> findAllWithOrder();

}
