package com.blogger.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.blogger.dto.CommentDTO;
import com.blogger.entities.Comment;
import com.blogger.exception.CommentNotFoundException;
import com.blogger.repository.CommentRepository;
import com.blogger.service.CommentService;
import com.blogger.utils.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService{
	private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentDTO save(CommentDTO commentDao) {
        Comment comment = commentMapper.mapToEntity(commentDao);
        return commentMapper.mapToDTO(commentRepository.save(comment));
    }

    @Override
    public CommentDTO findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + id));
        return commentMapper.mapToDTO(comment);
    }

    @Override
    public List<CommentDTO> findAll() {
        List<Comment> commentDaos = commentRepository.findAll();
        return commentMapper.mapToDTO(commentDaos);
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
    
    
    @Override
    public Page<CommentDTO> findComments(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> resultPage = commentRepository.findAll(pageRequest);
        return resultPage.map(commentMapper::mapToDTO);
    }
}
