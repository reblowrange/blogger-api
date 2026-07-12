package com.blogger.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "ApiErrorResponse", description = "Standard API error response envelope")
public class ApiErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP status text", example = "Bad Request")
    private String error;

    @Schema(description = "Error message", example = "Validation failed for request body")
    private String message;

    @Schema(description = "Request path", example = "/api/blogs")
    private String path;

    @Schema(description = "Validation errors when present", example = "[\"*Please provide title\",\"*Please provide blog\"]")
    private List<String> errors;
}
