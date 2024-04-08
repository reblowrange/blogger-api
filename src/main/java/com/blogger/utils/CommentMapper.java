package com.blogger.utils;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import com.blogger.dto.CommentDTO;
import com.blogger.entities.Comment;

@Component
public class CommentMapper {
	 private final ModelMapper modelMapper;

	    public CommentMapper() {
	        this.modelMapper =new ModelMapper();
	    }

	    public CommentDTO mapToDTO(Comment comment) {
	        return modelMapper.map(comment, CommentDTO.class);
	    }

	    public Comment mapToEntity(CommentDTO commentDTO) {
	        return modelMapper.map(commentDTO, Comment.class);
	    }
	    
		public List<CommentDTO> mapToDTO(List<Comment> comments) {
	        List<CommentDTO> commentDtos = modelMapper.map(comments, new TypeToken<List<CommentDTO>>() {}.getType());
	        return commentDtos;
		}
}
