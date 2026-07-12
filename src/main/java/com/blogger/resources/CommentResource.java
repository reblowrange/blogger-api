package com.blogger.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogger.dto.CommentDTO;
import com.blogger.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "Comment management endpoints")
@RequiredArgsConstructor
public class CommentResource {

	private final CommentService commentService;

	@Operation(summary = "Get comment by id", description = "Fetches a single comment by id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Comment found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class), examples = @ExampleObject(value = "{\"id\":10,\"comment\":\"Great post. The JWT filter flow is very clear.\",\"createdBy\":\"swapnil\",\"createdOn\":\"2026-07-12\"}"))),
			@ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Not Found\",\"message\":\"Comment not found with id: 10\"}"))),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	})
	@GetMapping("/{id}")
	public ResponseEntity<CommentDTO> getCommentById(@Parameter(description = "Comment id", example = "10") @PathVariable Long id) {
		CommentDTO comment = commentService.findById(id);
		return ResponseEntity.ok(comment);
	}

	    @Operation(summary = "Delete comment", description = "Deletes a comment by id. Only the comment owner can delete their comment.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Comment deleted", content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden - only owner can delete", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Forbidden\",\"message\":\"You can only delete your own comments\"}"))),
			@ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Not Found\",\"message\":\"Comment not found with id: 10\"}"))),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteComment(@Parameter(description = "Comment id", example = "10") @PathVariable Long id) {
		commentService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
