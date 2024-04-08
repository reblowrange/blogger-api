package com.blogger.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;

    @NotEmpty(message = "*Please write something")
    private String comment;

    private Date createdOn;

    private BlogDTO blog;

    private UserRegistrationDto user;
}
