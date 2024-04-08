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
	BlogMapper() {
        this.modelMapper = new ModelMapper();
    }

    public BlogDTO mapToDTO(Blog blog) {
        return modelMapper.map(blog, BlogDTO.class);
    }

    public Blog mapToEntity(BlogDTO blogDTO) {
        return modelMapper.map(blogDTO, Blog.class);
    }

	public List<BlogDTO> mapToDTO(List<Blog> blogs) {
        List<BlogDTO> blogDtos = modelMapper.map(blogs, new TypeToken<List<BlogDTO>>() {}.getType());
        return blogDtos;
	}
}
