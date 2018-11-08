package com.mz.imtaz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mz.imtaz.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	List<User> findByEnabledOrderByFullname(@Param("isEnabled") Boolean enabled);
}
