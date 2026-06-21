package com.blogapi.service;

import com.blogapi.exception.ResourceNotFoundException;
import com.blogapi.model.dto.CommentRequest;
import com.blogapi.model.dto.CommentResponse;
import com.blogapi.model.entity.Comment;
import com.blogapi.model.entity.Post;
import com.blogapi.repository.CommentRepository;
import com.blogapi.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsForPost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }
        return commentRepository.findByPostId(postId).stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getPendingComments() {
        return commentRepository.findByApprovedFalse().stream().map(this::mapToResponse).toList();
    }

    public CommentResponse createComment(CommentRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + request.getPostId()));
        Comment comment = new Comment(request.getContent(), request.getAuthor(), post);
        comment.setApproved(false);
        Comment saved = commentRepository.save(comment);
        logger.info("Created comment {} for post {}", saved.getId(), post.getId());
        return mapToResponse(saved);
    }

    public CommentResponse approveComment(Long id) {
        Comment comment = findComment(id);
        comment.setApproved(true);
        return mapToResponse(commentRepository.save(comment));
    }

    public void deleteComment(Long id) {
        commentRepository.delete(findComment(id));
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
    }

    private CommentResponse mapToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor(),
                comment.isApproved(),
                comment.getPost().getId(),
                comment.getCreatedAt()
        );
    }
}
