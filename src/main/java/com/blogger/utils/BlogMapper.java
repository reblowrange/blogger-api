package com.blogger.utils;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import com.blogger.dto.BlogDTO;
import com.blogger.entities.Blog;

@Component
public class BlogMapper {
	private final ModelMapper modelMapper;
    private final CommentMapper commentMapper;
    
	BlogMapper(CommentMapper commentMapper) {
        this.modelMapper = new ModelMapper();
        this.commentMapper = commentMapper;
    }

    public BlogDTO mapToDTO(Blog blog) {
        BlogDTO blogDTO = modelMapper.map(blog, BlogDTO.class);
        if (blog.getUser() != null) {
            blogDTO.setCreatedBy(blog.getUser().getUsername());
        }
        if (blog.getComments() != null) {
            blogDTO.setComments(commentMapper.mapToDTO(blog.getComments()));
        }
        return blogDTO;
    }

    public Blog mapToEntity(BlogDTO blogDTO) {
        return modelMapper.map(blogDTO, Blog.class);
    }

	public List<BlogDTO> mapToDTO(List<Blog> blogs) {
        List<BlogDTO> blogDtos = modelMapper.map(blogs, new TypeToken<List<BlogDTO>>() {}.getType());
        for (int i = 0; i < blogs.size(); i++) {
            if (blogs.get(i).getUser() != null) {
                blogDtos.get(i).setCreatedBy(blogs.get(i).getUser().getUsername());
            }
            if (blogs.get(i).getComments() != null) {
                blogDtos.get(i).setComments(commentMapper.mapToDTO(blogs.get(i).getComments()));
            }
        }
        return blogDtos;
	}
}
