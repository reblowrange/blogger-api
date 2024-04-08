package com.blogger.dto;

import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("access_token_expiry")
    private int accessTokenExpiry;

    @JsonProperty("token_type")
    private TokenType tokenType;
    
    @JsonProperty("username")
    private String userName;
}
