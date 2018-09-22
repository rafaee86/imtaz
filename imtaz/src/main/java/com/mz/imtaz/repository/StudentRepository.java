package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Query("Select a from Student a where a.name like %:name% order by a.name asc")
	List<Student> findByName(@Param("name")String name);

	@Query("Select a from Student a where UPPER(a.name) like %:name% ")
	List<Student> findByNamePageable(@Param("name")String name, Pageable page);
}
