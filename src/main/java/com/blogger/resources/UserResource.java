package com.blogger.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogger.dto.ApiErrorResponse;
import com.blogger.dto.UserProfileDto;
import com.blogger.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "User profile endpoints")
public class UserResource {

    private final UserService userService;

    @Operation(summary = "Get current user profile", description = "Returns the profile of the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile fetched", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class), examples = @ExampleObject(value = "{\"firstName\":\"Alex\",\"lastName\":\"Johnson\",\"email\":\"alex.johnson@example.com\",\"username\":\"alexj\",\"roles\":\"ROLE_BLOGGER\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }

    @Operation(summary = "Update current user profile", description = "Updates firstName, lastName, and email of the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class), examples = @ExampleObject(value = "{\"firstName\":\"Alexander\",\"lastName\":\"Johnson\",\"email\":\"alex.johnson@example.com\",\"username\":\"alexj\",\"roles\":\"ROLE_BLOGGER\"}"))),
                @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
                @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
                @ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(
            @Valid @RequestBody UserProfileDto profileDto) {
        return ResponseEntity.ok(userService.updateUserProfile(profileDto));
    }
}
