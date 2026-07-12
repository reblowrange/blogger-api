package com.blogger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(name = "UserProfile", description = "User profile payload")
public class UserProfileDto {

    @Schema(description = "First name", example = "Alex")
    @NotEmpty(message = "*Please provide your first name")
    private String firstName;

    @Schema(description = "Last name", example = "Johnson")
    @NotEmpty(message = "*Please provide your last name")
    private String lastName;

    @Schema(description = "Email", example = "alex.johnson@example.com")
    @Email(message = "*Please provide a valid email")
    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Schema(description = "Username", example = "alexj", accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    @Schema(description = "Roles", example = "ROLE_BLOGGER", accessMode = Schema.AccessMode.READ_ONLY)
    private String roles;
}
