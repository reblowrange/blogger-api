package com.blogger.utils;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.blogger.dto.UserRegistrationDto;
import com.blogger.entities.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {
	   private final ModelMapper modelMapper;
	   private final PasswordEncoder passwordEncoder;
	   

	    public UserRegistrationDto toDto(User user) {
	        UserRegistrationDto userRegistrationDto = modelMapper.map(user, UserRegistrationDto.class);
	        return userRegistrationDto;
	    }

	    public User toEntity(UserRegistrationDto userRegistrationDto) {
	        User user = modelMapper.map(userRegistrationDto, User.class);
	        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
			return user;
	    }
}
