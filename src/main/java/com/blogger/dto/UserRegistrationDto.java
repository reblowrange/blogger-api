package com.blogger.dto;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(name = "UserRegistration", description = "User signup payload")
public class UserRegistrationDto {

	@NotEmpty(message = "*Please provide your name")
	@Schema(description = "First name", example = "Alex")
	private String firstName;

	@NotEmpty(message = "*Please provide your last name")
	@Schema(description = "Last name", example = "Johnson")
	private String lastName;

	@Email(message = "*Please provide a valid Email")
	@NotEmpty(message = "*Please provide an email")
	@Schema(description = "Email", example = "alex.johnson@example.com")
	private String email;

	@Length(min = 5, message = "*Your username must have at least 5 characters")
	@NotEmpty(message = "*Please provide your name")
	@Schema(description = "Username", example = "alexj")
	private String username;

	@Length(min = 5, message = "*Your password must have at least 5 characters")
	@NotEmpty(message = "*Please provide your password")
	@Schema(description = "Password", example = "secret123", accessMode = Schema.AccessMode.WRITE_ONLY)
	private String password;
	
//	@NotEmpty(message="At leaste one role should be there e.g. 'ROLE_BLOGGER'")
	@Schema(description = "Roles", example = "ROLE_BLOGGER")
	private String roles;
}
