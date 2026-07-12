package com.blogger.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.blogger.dto.BlogDTO;
import com.blogger.entities.Blog;
import com.blogger.entities.User;
import com.blogger.exception.BlogNotFoundException;
import com.blogger.exception.UnauthorizedException;
import com.blogger.repository.BlogRepository;
import com.blogger.service.BlogService;
import com.blogger.service.UserContextService;
import com.blogger.service.UserService;
import com.blogger.utils.BlogMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
	private final BlogRepository blogRepository;
	private final BlogMapper blogMapper;
	private final UserContextService userContextService;
	private final UserService userService;

    @Override
    public BlogDTO createBlog(BlogDTO blogDao) {
    	Blog blog = blogMapper.mapToEntity(blogDao);
    	
    	User user = userContextService.getUser();
    	blog.setUser(user);
        return blogMapper.mapToDTO(blogRepository.save(blog));
    }

    @Override
    public BlogDTO getBlogById(Long id) {
        Blog comment = blogRepository.findById(id)
                .orElseThrow(() -> new BlogNotFoundException("Blog not found with ID: " + id));
        return blogMapper.mapToDTO(comment);
    }

    @Override
    public List<BlogDTO> getAllBlogs() {
        return blogMapper.mapToDTO((List<Blog>) blogRepository.findAll());
    }

    @Override
    public void deleteBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new BlogNotFoundException("Blog not found with ID: " + id));
        
        User currentUser = userContextService.getUser();
        if (!blog.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only delete your own blogs.");
        }
        
        blogRepository.deleteById(id);
    }
    
    @Override
    public Page<BlogDTO> findBlogs(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Blog> resultPage = blogRepository.findAll(pageRequest);
        return resultPage.map(blogMapper::mapToDTO);
    }

    @Override
    public BlogDTO updateBlog(Long id, BlogDTO blogDao) {
        Blog existing = blogRepository.findById(id)
                .orElseThrow(() -> new BlogNotFoundException("Blog not found with ID: " + id));
        
        User currentUser = userContextService.getUser();
        if (!existing.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only edit your own blogs.");
        }
        
        existing.setTitle(blogDao.getTitle());
        existing.setBlog(blogDao.getBlog());
        return blogMapper.mapToDTO(blogRepository.save(existing));
    }

    @Override
    public List<BlogDTO> getBlogsByUsername(String username) {
        return blogMapper.mapToDTO(blogRepository.findByUserUsername(username));
    }

    @Override
    public List<BlogDTO> searchBlogs(String keyword) {
        return blogMapper.mapToDTO(
                blogRepository.findByTitleContainingIgnoreCaseOrBlogContainingIgnoreCase(keyword, keyword));
    }
}
