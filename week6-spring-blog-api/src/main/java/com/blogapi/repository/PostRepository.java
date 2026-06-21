package com.blogapi.repository;

import com.blogapi.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryId(Long categoryId);

    Page<Post> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
}
