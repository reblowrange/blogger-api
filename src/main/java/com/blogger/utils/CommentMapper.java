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
	        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
	        if (comment.getUser() != null) {
	            commentDTO.setCreatedBy(comment.getUser().getUsername());
	        }
	        return commentDTO;
	    }

	    public Comment mapToEntity(CommentDTO commentDTO) {
	        return modelMapper.map(commentDTO, Comment.class);
	    }
	    
		public List<CommentDTO> mapToDTO(List<Comment> comments) {
	        List<CommentDTO> commentDtos = modelMapper.map(comments, new TypeToken<List<CommentDTO>>() {}.getType());
	        for (int i = 0; i < comments.size(); i++) {
	            if (comments.get(i).getUser() != null) {
	                commentDtos.get(i).setCreatedBy(comments.get(i).getUser().getUsername());
	            }
	        }
	        return commentDtos;
		}
}
