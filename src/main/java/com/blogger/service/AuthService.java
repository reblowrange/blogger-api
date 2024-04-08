package com.blogger.service;

import org.springframework.security.core.Authentication;

import com.blogger.dto.AuthResponseDto;
import com.blogger.dto.UserRegistrationDto;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response);

	public Object getAccessTokenUsingRefreshToken(String authorizationHeader);

	public AuthResponseDto registerUser(UserRegistrationDto userRegistrationDto, HttpServletResponse httpServletResponse);
}
