package com.blogger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogger.entities.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByUserUsername(String username);

    List<Blog> findByTitleContainingIgnoreCaseOrBlogContainingIgnoreCase(String title, String blog);
}
