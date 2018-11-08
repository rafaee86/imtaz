package com.mz.imtaz;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mz.imtaz.entity.User;
import com.mz.imtaz.entity.UserContext;
import com.mz.imtaz.repository.UserRepository;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserContext loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = new User();
		user.setUsername(username);
		Example<User> userQBE = Example.of(user);
		
		Optional<User> userOptinal = userRepo.findOne(userQBE);
		
		return new UserContext(userOptinal.orElse(null));
	} 

}
