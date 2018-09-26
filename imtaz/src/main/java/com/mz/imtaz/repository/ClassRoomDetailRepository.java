package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.ClassRoom;
import com.mz.imtaz.entity.ClassRoomDetail;

public interface ClassRoomDetailRepository extends JpaRepository<ClassRoomDetail, Integer> {

	@Query("Select a from ClassRoomDetail a order by a.classRoom.level, a.name, a.teacher.name asc")
	List<ClassRoomDetail> findAllWithOrder();

	@Query("Select a from ClassRoomDetail a where a.classRoom = :classRoom order by a.classRoom.level, a.teacher.name asc")
	List<ClassRoomDetail> findByClassRoom(@Param("classRoom") ClassRoom classRoom);

}
