package com.blogger.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.blogger.entities.User;

@Service
public class UserContextService {
	private Authentication authentication;
	private UserDetails userDetails;
	@Autowired
	private UserService userService;

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userDetails.getAuthorities();
	}

	public User getUser() {

		authentication = SecurityContextHolder.getContext().getAuthentication();

		return userService.findByEmail(authentication.getName())
				.orElseThrow(() -> new UsernameNotFoundException("UserEmail: " + authentication.getName() + " does not exist"));

//		this.mapUserDetails();
//		return userService.findByEmail(userDetails.getUsername())
//				.orElseThrow(() -> new UsernameNotFoundException("UserEmail: " + userDetails.getUsername() + " does not exist"));
	}

}
