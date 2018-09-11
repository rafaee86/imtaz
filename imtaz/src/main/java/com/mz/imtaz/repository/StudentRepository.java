package com.mz.imtaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mz.imtaz.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
