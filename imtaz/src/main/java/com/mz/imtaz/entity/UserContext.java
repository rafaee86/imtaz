package com.mz.imtaz.entity;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.Getter;

@Getter
public class UserContext implements UserDetails{
	
	private static final long serialVersionUID = -7482528559683647724L;
	private User user;
	
	public UserContext(User user) {
		this.user = user;
		if(this.user == null) {
			throw new UsernameNotFoundException("User not valid.");
		}
		
		this.pkid = this.user.getPkid();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	
	public Integer pkid;

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		if(user.getExpiredDate() != null) {
			return LocalDate.now().isBefore(user.getExpiredDate());
		}
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !(user.getIsLock() != null && user.getIsLock());
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.getEnabled();
	}

}
