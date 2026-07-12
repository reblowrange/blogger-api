package com.blogger.repository;

import java.util.List;

import com.blogger.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBlogId(Long blogId);
}	