package com.blogger.service.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.blogger.repository.RefreshTokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutHandlerServiceImpl implements LogoutHandler {
	private final RefreshTokenRepository refreshTokenRepo;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if(!authHeader.startsWith(TokenType.BEARER.getValue())){
            return;
        }

        final String refreshToken = authHeader.substring(7);
        
        var storedRefreshToken = refreshTokenRepo.findByRefreshToken(refreshToken)
                .map(token->{
                    token.setRevoked(true);
                    refreshTokenRepo.save(token);
                    return token;
                })
                .orElse(null);
    }
}
