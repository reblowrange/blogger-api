package com.blogger.dto;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(name = "UsernameNPasswordRequest", description = "Username and password sign-in request payload")
public class UsernameNPasswordRequestDto {

	@NotEmpty(message = "*Please provide username")
	@Schema(description = "Username or email", example = "alexj")
	private String username;

	@Length(min = 5, message = "*Your password must have at least 5 characters")
	@NotEmpty(message = "*Please provide your password")
	@Schema(description = "Password", example = "secret123", accessMode = Schema.AccessMode.WRITE_ONLY)
	private String password;
}