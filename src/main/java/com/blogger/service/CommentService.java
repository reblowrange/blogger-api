package com.blogger.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.blogger.dto.CommentDTO;

public interface CommentService {
    CommentDTO save(CommentDTO commentDao);
    CommentDTO findById(Long id);
    List<CommentDTO> findAll();
    void deleteById(Long id);
	Page<CommentDTO> findComments(int page, int size);
}
