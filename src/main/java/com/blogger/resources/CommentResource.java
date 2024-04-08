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

@RestController
@RequestMapping("/api/comments")
public class CommentResource {
	@Autowired
	 private CommentService commentService;

	    @PostMapping
	    public ResponseEntity<CommentDTO> saveComment(@RequestBody CommentDTO comment) {
	        CommentDTO savedComment = commentService.save(comment);
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
	        CommentDTO comment = commentService.findById(id);
	        return ResponseEntity.ok(comment);
	    }

	    @GetMapping
	    public ResponseEntity<List<CommentDTO>> getAllComments() {
	        List<CommentDTO> comments = commentService.findAll();
	        return ResponseEntity.ok(comments);
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
	        commentService.deleteById(id);
	        return ResponseEntity.noContent().build();
	    }

	    @GetMapping("/paged")
	    public ResponseEntity<Page<CommentDTO>> findComments(@RequestParam int page, @RequestParam int size) {
	        Page<CommentDTO> commentPage = commentService.findComments(page, size);
	        return ResponseEntity.ok(commentPage);
	    }
}
