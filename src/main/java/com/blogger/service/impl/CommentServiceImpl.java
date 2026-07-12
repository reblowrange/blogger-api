package com.blogger.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.blogger.dto.CommentDTO;
import com.blogger.entities.Blog;
import com.blogger.entities.Comment;
import com.blogger.entities.User;
import com.blogger.exception.BlogNotFoundException;
import com.blogger.exception.CommentNotFoundException;
import com.blogger.repository.BlogRepository;
import com.blogger.repository.CommentRepository;
import com.blogger.service.CommentService;
import com.blogger.service.UserContextService;
import com.blogger.utils.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BlogRepository blogRepository;
    private final UserContextService userContextService;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper,
                              BlogRepository blogRepository, UserContextService userContextService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.blogRepository = blogRepository;
        this.userContextService = userContextService;
    }

    @Override
    public CommentDTO save(CommentDTO commentDao) {
        Comment comment = commentMapper.mapToEntity(commentDao);
        return commentMapper.mapToDTO(commentRepository.save(comment));
    }

    @Override
    public CommentDTO saveCommentToBlog(Long blogId, CommentDTO commentDto) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog not found with ID: " + blogId));
        User user = userContextService.getUser();
        Comment comment = commentMapper.mapToEntity(commentDto);
        comment.setBlog(blog);
        comment.setUser(user);
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

    @Override
    public List<CommentDTO> findByBlogId(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog not found with ID: " + blogId));
        List<Comment> comments = commentRepository.findByBlogId(blogId);
        return commentMapper.mapToDTO(comments);
    }
}
