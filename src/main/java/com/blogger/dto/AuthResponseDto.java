package com.blogger.dto;

import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "AuthResponse", description = "Authentication response payload")
public class AuthResponseDto {
    @JsonProperty("access_token")
    @Schema(description = "JWT access token", example = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhbGV4aiJ9.signature")
    private String accessToken;
    
    @JsonProperty("access_token_expiry")
    @Schema(description = "Access token expiry time in seconds", example = "300")
    private int accessTokenExpiry;

    @JsonProperty("token_type")
    @Schema(description = "Token type", example = "Bearer")
    private TokenType tokenType;
    
    @JsonProperty("full_name")
    @Schema(description = "Full name", example = "Alex Johnson")
    private String fullName;
    
    @JsonProperty("username")
    @Schema(description = "Username", example = "alexj")
    private String username;
    
    @JsonProperty("email")
    @Schema(description = "Email", example = "alex.johnson@example.com")
    private String email;
    
    @JsonProperty("roles")
    @Schema(description = "Granted roles", example = "ROLE_BLOGGER")
    private String roles;
}
