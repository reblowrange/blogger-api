package com.blogger.service.impl;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.blogger.dto.AuthResponseDto;
import com.blogger.dto.UserRegistrationDto;
import com.blogger.entities.RefreshToken;
import com.blogger.entities.User;
import com.blogger.repository.RefreshTokenRepository;
import com.blogger.repository.UserRepository;
import com.blogger.service.AuthService;
import com.blogger.utils.JwtTokenGenerator;
import com.blogger.utils.UserMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userInfoRepo;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepo;
    private final UserMapper userMapper;
    
	@Override
	public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
		try
        {
            var userInfoEntity = userInfoRepo.findByEmail(authentication.getName())
                    .orElseThrow(()->{
                        log.error("[AuthService:userSignInAuth] User :{} not found",authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");});


            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

//            Persist in DB & Refresh token and set in response 
            saveUserRefreshToken(userInfoEntity,refreshToken);
            creatRefreshTokenCookie(response,refreshToken);
            
            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated",userInfoEntity.getUsername());
            return  AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(60)
                    .userName(userInfoEntity.getUsername())
                    .tokenType(TokenType.BEARER)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
	}
	
    private void saveUserRefreshToken(User userInfoEntity, String refreshToken) {
        var refreshTokenEntity = RefreshToken.builder()
                .user(userInfoEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
    }
    
    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {
    	 
        if(!authorizationHeader.startsWith(TokenType.BEARER.getValue())){
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.  
        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(tokens-> !tokens.isRevoked())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));

        User userInfoEntity = refreshTokenEntity.getUser();
        
        //Now create the Authentication object
        Authentication authentication =  createAuthenticationObject(userInfoEntity);

        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role. 
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return  AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(userInfoEntity.getUsername())
                .tokenType(TokenType.BEARER)
                .build();
    }
    
    @Override
    public AuthResponseDto registerUser(UserRegistrationDto userRegistrationDto,HttpServletResponse httpServletResponse){

        try{
            log.info("[AuthService:registerUser]User Registration Started with :::{}",userRegistrationDto);

            Optional<User> user = userInfoRepo.findByEmail(userRegistrationDto.getEmail());
            if(user.isPresent()){
                throw new Exception("User Already Exist");
            }

            User userDetailsEntity = userMapper.toEntity(userRegistrationDto);
            Authentication authentication = createAuthenticationObject(userDetailsEntity);


            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            User savedUserDetails = userInfoRepo.save(userDetailsEntity);
            saveUserRefreshToken(userDetailsEntity,refreshToken);
            
            creatRefreshTokenCookie(httpServletResponse,refreshToken);
            
            log.info("[AuthService:registerUser] User:{} Successfully registered",savedUserDetails.getFirstName() + " with Email: " + savedUserDetails.getEmail());
            return   AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .userName(savedUserDetails.getUsername())
                    .tokenType(TokenType.BEARER)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }

    }
    
    private static Authentication createAuthenticationObject(User userInfoEntity) {
        // Extract user details from UserDetailsEntity
        String username = userInfoEntity.getEmail();
        String password = userInfoEntity.getPassword();
        String roles = userInfoEntity.getRoles();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }
    
    
}
