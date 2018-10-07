package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mz.imtaz.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

	@Query("Select a from Teacher a where a.recordUtility.statusFlag = true ")
	List<Teacher> findAllActive(Sort sort);
}
