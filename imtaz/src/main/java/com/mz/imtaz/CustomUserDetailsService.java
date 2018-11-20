package com.mz.imtaz;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
		
		Optional<User> userOptional = userRepo.findOne(userQBE);
		
		if("Administrator".equals(username) && !userOptional.isPresent()) {
			return new UserContext(getAdministratorUser());
		}
		
		return new UserContext(userOptional.orElse(null));
	} 
	
	private User getAdministratorUser() {
		
		User user = new User();
		user.setUsername("Administrator");
		user.setPlainPassword("Administrator");
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPlainPassword()));
		user.setFullname("Administrator");
		user.setEnabled(true);
		user.setIsLock(false);
		user.setExpiredDate(null);
		user.setIsAdministrator(true);
		
		return user;
	}

}
