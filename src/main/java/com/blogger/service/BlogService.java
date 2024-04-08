package com.blogger.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.blogger.dto.BlogDTO;

public interface BlogService {
	BlogDTO createBlog(BlogDTO blog);
    BlogDTO getBlogById(Long id);
    List<BlogDTO> getAllBlogs();
    void deleteBlog(Long id);
	Page<BlogDTO> findBlogs(int page, int size);
}
