package com.blogger.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserRegistrationDto {

	@NotEmpty(message = "*Please provide your name")
	private String firstName;

	@NotEmpty(message = "*Please provide your last name")
	private String lastName;

	@Email(message = "*Please provide a valid Email")
	@NotEmpty(message = "*Please provide an email")
	private String email;

	@Length(min = 5, message = "*Your username must have at least 5 characters")
	@NotEmpty(message = "*Please provide your name")
	private String username;

	@Length(min = 5, message = "*Your password must have at least 5 characters")
	@NotEmpty(message = "*Please provide your password")
	private String password;
	
	@NotEmpty(message="At leaste one role should be there e.g. 'ROLE_BLOGGER'")
	private String roles;
}
