package com.blogapi.model.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String content;
    private String author;
    private boolean approved;
    private Long postId;
    private LocalDateTime createdAt;

    public CommentResponse(Long id, String content, String author, boolean approved, Long postId,
                           LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.approved = approved;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isApproved() {
        return approved;
    }

    public Long getPostId() {
        return postId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
