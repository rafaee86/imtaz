package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.ClassRoomDetail;
import com.mz.imtaz.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Query("Select a from Student a "
			+ "where a.recordUtility.statusFlag = true "
			+ "and a.name like %:name% "
			+ "order by a.name asc")
	List<Student> findByName(@Param("name")String name, Sort sort);

	@Query("Select a from Student a "
			+ "where a.recordUtility.statusFlag = true "
			+ "and UPPER(a.name) like %:name% "
			+ "order by a.name asc")
	List<Student> findByNamePageable(@Param("name")String name, Pageable page);

	@Query("Select a from Student a "
			+ "where a.recordUtility.statusFlag = true "
			+ "and a.pkid in ("
			+ "				Select b.student.pkid from Records b "
			+ "				where b.recordUtility.statusFlag = true "
			+ "				and b.pkid = (select max(ir.pkid) from Records ir where ir.student = b.student) "
			+ "				and b.classRoomDetail = :classRoomDetail"
			+ ")")
	List<Student> findByClassRoomDetail(@Param("classRoomDetail") ClassRoomDetail classRoomDetail);
	
	@Query("Select a from Student a "
			+ "where a.recordUtility.statusFlag = true "
			+ "and a.pkid in ("
			+ "					Select b.student.pkid from Records b "
			+ "					where (b.recordUtility.statusFlag = true or (b.recordUtility.statusFlag = false and b.status in ("
			+ "							select gc.pkid from "
			+ "							GeneralCode gc "
			+ "							where gc.code in ('Q','E','X')))) "
			+ "							and b.classRoomDetail = :classRoomDetail"
			+ ")")
	List<Student> findByClassRoomDetailForStudentHistory(@Param("classRoomDetail") ClassRoomDetail classRoomDetail);
	
	@Query("Select a from Student a "
			+ "where a.recordUtility.statusFlag = true")
	List<Student> findAllActive(Sort sort);
	
	@Query("Select a from Student a "
			+ "where a.recordUtility.statusFlag = true "
			+ "order by a.name asc")
	List<Student> findAllActive(Pageable pageable);
	
	@Query(value = "SELECT LPAD( " + 
			"	CONVERT( " + 
			"		RIGHT(IFNULL((SELECT MAX(STUDENT_NO) FROM STUDENT  " + 
			"		WHERE STUDENT_NO LIKE :prefix ),'0'),:lpadLength), " + 
			"	 UNSIGNED INTEGER " + 
			"	) " + 
			"+1,:lpadLength,'0') FROM DUAL", nativeQuery = true)
	String getStudentNoMax(@Param("prefix") String prefix, @Param("lpadLength") Integer lpadLength);
}
