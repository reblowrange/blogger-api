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

@RestController
@RequestMapping("/api/blogs")
public class BlogResource {
	@Autowired
	private BlogService blogService;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PostMapping
	public ResponseEntity<BlogDTO> createBlog(@RequestBody BlogDTO blog) {
		BlogDTO createdBlog = blogService.createBlog(blog);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BlogDTO> getBlogById(@PathVariable Long id) {
		BlogDTO blog = blogService.getBlogById(id);
		return ResponseEntity.ok(blog);
	}

	@GetMapping
	public ResponseEntity<List<BlogDTO>> getAllBlogs() {
		List<BlogDTO> blogs = blogService.getAllBlogs();
		return ResponseEntity.ok(blogs);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
		blogService.deleteBlog(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/paged")
	public ResponseEntity<Page<BlogDTO>> findBlogs(@RequestParam int page, @RequestParam int size) {
		Page<BlogDTO> blogPage = blogService.findBlogs(page, size);
		return ResponseEntity.ok(blogPage);
	}
}
