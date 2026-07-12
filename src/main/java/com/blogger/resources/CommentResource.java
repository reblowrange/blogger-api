package com.blogger.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "Comment management endpoints")
public class CommentResource {
	@Autowired
	 private CommentService commentService;

	    @Operation(summary = "Create comment", description = "Creates a new comment.")
	    @ApiResponses(value = {
	            @ApiResponse(responseCode = "201", description = "Comment created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class), examples = @ExampleObject(value = "{\"id\":10,\"comment\":\"Great post. The JWT filter flow is very clear.\",\"createdOn\":\"2026-07-12\"}"))),
	            @ApiResponse(responseCode = "400", description = "Invalid comment payload", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Bad Request\",\"message\":\"Validation failed for request body\"}"))),
	            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	    })
	    @PostMapping
	    public ResponseEntity<CommentDTO> saveComment(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
	            required = true,
	            description = "Comment payload",
	            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class), examples = @ExampleObject(value = "{\"comment\":\"Great post. The JWT filter flow is very clear.\"}"))
	    ) CommentDTO comment) {
	        CommentDTO savedComment = commentService.save(comment);
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
	    }

	    @Operation(summary = "Get comment by id", description = "Fetches a single comment by id.")
	    @ApiResponses(value = {
	            @ApiResponse(responseCode = "200", description = "Comment found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class), examples = @ExampleObject(value = "{\"id\":10,\"comment\":\"Great post. The JWT filter flow is very clear.\",\"createdOn\":\"2026-07-12\"}"))),
	            @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Not Found\",\"message\":\"Comment not found with id: 10\"}"))),
	            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	    })
	    @GetMapping("/{id}")
	    public ResponseEntity<CommentDTO> getCommentById(@Parameter(description = "Comment id", example = "10") @PathVariable Long id) {
	        CommentDTO comment = commentService.findById(id);
	        return ResponseEntity.ok(comment);
	    }

	    @Operation(summary = "Get all comments", description = "Returns all comments.")
	    @ApiResponses(value = {
	            @ApiResponse(responseCode = "200", description = "Comments fetched", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class), examples = @ExampleObject(value = "[{\"id\":10,\"comment\":\"Great post. The JWT filter flow is very clear.\",\"createdOn\":\"2026-07-12\"}]"))),
	            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	    })
	    @GetMapping
	    public ResponseEntity<List<CommentDTO>> getAllComments() {
	        List<CommentDTO> comments = commentService.findAll();
	        return ResponseEntity.ok(comments);
	    }

	    @Operation(summary = "Delete comment", description = "Deletes a comment by id.")
	    @ApiResponses(value = {
	            @ApiResponse(responseCode = "204", description = "Comment deleted", content = @Content),
	            @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Not Found\",\"message\":\"Comment not found with id: 10\"}"))),
	            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	    })
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteComment(@Parameter(description = "Comment id", example = "10") @PathVariable Long id) {
	        commentService.deleteById(id);
	        return ResponseEntity.noContent().build();
	    }

	    @Operation(summary = "Get paged comments", description = "Returns comments in paginated format.")
	    @ApiResponses(value = {
	            @ApiResponse(responseCode = "200", description = "Paged comments fetched", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"content\":[{\"id\":10,\"comment\":\"Great post. The JWT filter flow is very clear.\",\"createdOn\":\"2026-07-12\"}],\"pageable\":{\"pageNumber\":0,\"pageSize\":10},\"totalElements\":1,\"totalPages\":1,\"last\":true,\"first\":true,\"numberOfElements\":1,\"size\":10,\"number\":0,\"empty\":false}"))),
	            @ApiResponse(responseCode = "400", description = "Invalid paging parameters", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Bad Request\",\"message\":\"page and size must be positive integers\"}"))),
	            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	    })
	    @GetMapping("/paged")
	    public ResponseEntity<Page<CommentDTO>> findComments(@Parameter(description = "Zero-based page index", example = "0") @RequestParam int page,
	                                                        @Parameter(description = "Page size", example = "10") @RequestParam int size) {
	        Page<CommentDTO> commentPage = commentService.findComments(page, size);
	        return ResponseEntity.ok(commentPage);
	    }
}
