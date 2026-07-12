package com.blogger.dto;

import java.sql.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(name = "Comment", description = "Comment payload")
public class CommentDTO {
    @Schema(description = "Comment id", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotEmpty(message = "*Please write something")
    @Schema(description = "Comment text", example = "Great post. The JWT filter flow is very clear.")
    private String comment;

    @Schema(description = "Creation date", example = "2026-07-12", accessMode = Schema.AccessMode.READ_ONLY)
    private Date createdOn;

    @Schema(hidden = true)
    private BlogDTO blog;

    @Schema(hidden = true)
    private UserRegistrationDto user;
}
