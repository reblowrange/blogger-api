package com.blogger.dto;

import java.sql.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BlogDTO {
	private Long id;

	@Length(min = 5, message = "*Your title must have at least 5 characters")
	@NotEmpty(message = "*Please provide title")
	private String title;

	@Length(min = 25, message = "*Your blog must have at least 25 characters")
	@NotEmpty(message = "*Please provide blog")
	private String blog;

	private Date createdOn;

	@JsonIgnore
	private UserRegistrationDto user;
	
	private List<CommentDTO> comments;
}
