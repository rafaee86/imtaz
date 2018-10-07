package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mz.imtaz.entity.ClassRoom;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Integer> {

	@Query("Select a from ClassRoom a where a.recordUtility.statusFlag = true and UPPER(a.name) = UPPER(:name) ")
	ClassRoom findByNameIgnoreCase(String name);
	
	@Query("Select a from ClassRoom a where a.recordUtility.statusFlag = true ")
	List<ClassRoom> findAllActive(Sort sort);
}
