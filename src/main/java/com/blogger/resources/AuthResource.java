package com.blogger.resources;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.blogger.dto.ApiErrorResponse;
import com.blogger.dto.UsernameNPasswordRequestDto;
import com.blogger.dto.UserRegistrationDto;
import com.blogger.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication and token endpoints")
public class AuthResource {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Sign in", description = "Authenticates user and returns JWT response.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authenticated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.blogger.dto.AuthResponseDto.class), examples = @ExampleObject(value = "{\"access_token\":\"eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhbGV4aiJ9.signature\",\"access_token_expiry\":300,\"token_type\":\"Bearer\",\"full_name\":\"Alex Johnson\",\"username\":\"alexj\",\"email\":\"alex.johnson@example.com\",\"roles\":\"ROLE_BLOGGER\"}"))),
        @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Sign-in request body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsernameNPasswordRequestDto.class), examples = @ExampleObject(value = "{\"username\":\"alexj\",\"password\":\"secret123\"}"))
    )
        @PostMapping("/sign-in/username-password")
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody UsernameNPasswordRequestDto signInRequestDto, HttpServletResponse response){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDto.getUsername(), signInRequestDto.getPassword()));

        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication, response));
    }
    
    @Operation(summary = "Refresh access token", description = "Generates a new access token using a valid refresh token.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Access token refreshed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.blogger.dto.AuthResponseDto.class), examples = @ExampleObject(value = "{\"access_token\":\"eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhbGV4aiJ9.signature\",\"access_token_expiry\":300,\"token_type\":\"Bearer\",\"full_name\":\"Alex Johnson\",\"username\":\"alexj\",\"email\":\"alex.johnson@example.com\",\"roles\":\"ROLE_BLOGGER\"}"))),
        @ApiResponse(responseCode = "401", description = "Refresh token is missing or invalid", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient scope", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@Parameter(description = "Bearer refresh token", example = "Bearer eyJhbGciOiJSUzI1NiJ9.refresh.signature") @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }
    
    @Operation(summary = "Register user", description = "Creates a new user account and returns authentication response.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.blogger.dto.AuthResponseDto.class), examples = @ExampleObject(value = "{\"access_token\":\"eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhbGV4aiJ9.signature\",\"access_token_expiry\":300,\"token_type\":\"Bearer\",\"full_name\":\"Alex Johnson\",\"username\":\"alexj\",\"email\":\"alex.johnson@example.com\",\"roles\":\"ROLE_BLOGGER\"}"))),
        @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\"*Please provide your name\",\"*Your username must have at least 5 characters\"]"))),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                  required = true,
                                                  description = "Signup request body",
                                                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRegistrationDto.class), examples = @ExampleObject(value = "{\"firstName\":\"Alex\",\"lastName\":\"Johnson\",\"email\":\"alex.johnson@example.com\",\"username\":\"alexj\",\"password\":\"secret123\",\"roles\":\"ROLE_BLOGGER\"}"))
                                          ) UserRegistrationDto userRegistrationDto,
                                          BindingResult bindingResult,HttpServletResponse httpServletResponse){

       log.info("[AuthController:registerUser]Signup Process Started for user:{}",userRegistrationDto.getUsername());
       if (bindingResult.hasErrors()) {
          List<String> errorMessage = bindingResult.getAllErrors().stream()
                  .map(DefaultMessageSourceResolvable::getDefaultMessage)
                  .toList();
          log.error("[AuthController:registerUser]Errors in user:{}",errorMessage);
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
       }
       return ResponseEntity.ok(authService.registerUser(userRegistrationDto,httpServletResponse));
    }
}
