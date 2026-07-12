package com.blogger.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "Blog", description = "Blog payload")
public class BlogDTO {
	@Schema(description = "Blog id", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	private Long id;

	@NotBlank(message = "*Please provide title")
	@Schema(description = "Blog title", example = "Spring Boot Security with JWT")
	private String title;

	@NotBlank(message = "*Please provide blog")
	@Schema(description = "Blog content", example = "This article explains JWT authentication in Spring Boot with practical examples.")
	private String blog;

	@Schema(description = "Creation date", example = "2026-07-12", accessMode = Schema.AccessMode.READ_ONLY)
	private Date createdOn;

	@Schema(description = "Username of the blog creator", example = "swapnil", accessMode = Schema.AccessMode.READ_ONLY)
	private String createdBy;

	@JsonIgnore
	@Schema(hidden = true)
	private UserRegistrationDto user;
	
	@Schema(description = "Comments associated with this blog", accessMode = Schema.AccessMode.READ_ONLY)
	private List<CommentDTO> comments = new ArrayList<>();
}
