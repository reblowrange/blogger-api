package com.blogger.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogger.dto.BlogDTO;
import com.blogger.service.BlogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/blogs")
@Tag(name = "Blogs", description = "Blog management endpoints")
public class BlogResource {
	@Autowired
	private BlogService blogService;

//	@PreAuthorize("hasAnyRole('ROLE_BLOGGER')")
	@Operation(summary = "Create blog", description = "Creates a new blog post.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Blog created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogDTO.class), examples = @ExampleObject(value = "{\"id\":1,\"title\":\"Spring Boot Security with JWT\",\"blog\":\"This article explains JWT authentication in Spring Boot with practical examples.\",\"createdOn\":\"2026-07-12\",\"comments\":[]}"))),
			@ApiResponse(responseCode = "400", description = "Invalid blog payload", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Bad Request\",\"message\":\"Validation failed for request body\"}"))),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	})
	@PostMapping
	public ResponseEntity<BlogDTO> createBlog(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
			required = true,
			description = "Blog payload",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogDTO.class), examples = @ExampleObject(value = "{\"title\":\"Spring Boot Security with JWT\",\"blog\":\"This article explains JWT authentication in Spring Boot with practical examples.\"}"))
	) BlogDTO blog) {
		BlogDTO createdBlog = blogService.createBlog(blog);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
	}

	@Operation(summary = "Get blog by id", description = "Fetches a single blog by id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Blog found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogDTO.class), examples = @ExampleObject(value = "{\"id\":1,\"title\":\"Spring Boot Security with JWT\",\"blog\":\"This article explains JWT authentication in Spring Boot with practical examples.\",\"createdOn\":\"2026-07-12\",\"comments\":[]}"))),
			@ApiResponse(responseCode = "404", description = "Blog not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Not Found\",\"message\":\"Blog not found with id: 1\"}"))),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	})
	@GetMapping("/{id}")
	public ResponseEntity<BlogDTO> getBlogById(@Parameter(description = "Blog id", example = "1") @PathVariable Long id) {
		BlogDTO blog = blogService.getBlogById(id);
		return ResponseEntity.ok(blog);
	}

	@Operation(summary = "Get all blogs", description = "Returns all blogs.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Blogs fetched", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogDTO.class), examples = @ExampleObject(value = "[{\"id\":1,\"title\":\"Spring Boot Security with JWT\",\"blog\":\"This article explains JWT authentication in Spring Boot with practical examples.\",\"createdOn\":\"2026-07-12\",\"comments\":[]}]"))),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	})
	@GetMapping
	public ResponseEntity<List<BlogDTO>> getAllBlogs() {
		List<BlogDTO> blogs = blogService.getAllBlogs();
		return ResponseEntity.ok(blogs);
	}

	@Operation(summary = "Delete blog", description = "Deletes a blog by id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Blog deleted", content = @Content),
			@ApiResponse(responseCode = "404", description = "Blog not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Not Found\",\"message\":\"Blog not found with id: 1\"}"))),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBlog(@Parameter(description = "Blog id", example = "1") @PathVariable Long id) {
		blogService.deleteBlog(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Get paged blogs", description = "Returns blogs in paginated format.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Paged blogs fetched", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"content\":[{\"id\":1,\"title\":\"Spring Boot Security with JWT\",\"blog\":\"This article explains JWT authentication in Spring Boot with practical examples.\",\"createdOn\":\"2026-07-12\",\"comments\":[]}],\"pageable\":{\"pageNumber\":0,\"pageSize\":10},\"totalElements\":1,\"totalPages\":1,\"last\":true,\"first\":true,\"numberOfElements\":1,\"size\":10,\"number\":0,\"empty\":false}"))),
			@ApiResponse(responseCode = "400", description = "Invalid paging parameters", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Bad Request\",\"message\":\"page and size must be positive integers\"}"))),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "An error occurred: <details>")))
	})
	@GetMapping("/paged")
	public ResponseEntity<Page<BlogDTO>> findBlogs(@Parameter(description = "Zero-based page index", example = "0") @RequestParam int page,
			@Parameter(description = "Page size", example = "10") @RequestParam int size) {
		Page<BlogDTO> blogPage = blogService.findBlogs(page, size);
		return ResponseEntity.ok(blogPage);
	}
}
