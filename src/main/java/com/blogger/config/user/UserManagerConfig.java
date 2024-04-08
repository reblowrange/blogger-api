package com.blogger.config.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.blogger.entities.User;
import com.blogger.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserManagerConfig implements UserDetailsService {
	private final UserService userService;

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		Optional<User> user = userService.findByEmail(emailId);
		return user.map(UserDetailsConfig::new)
				.orElseThrow(() -> new UsernameNotFoundException("UserEmail: " + emailId + " does not exist"));
	}
}
