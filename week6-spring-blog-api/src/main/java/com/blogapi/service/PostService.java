package com.blogapi.service;

import com.blogapi.exception.ResourceNotFoundException;
import com.blogapi.model.dto.PostRequest;
import com.blogapi.model.dto.PostResponse;
import com.blogapi.model.entity.Category;
import com.blogapi.model.entity.Post;
import com.blogapi.repository.CategoryRepository;
import com.blogapi.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public PostService(PostRepository postRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        return mapToResponse(findPost(id));
    }

    public PostResponse createPost(PostRequest request) {
        Category category = findCategory(request.getCategoryId());
        Post post = new Post(request.getTitle(), request.getContent(), request.getAuthor(), category);
        Post saved = postRepository.save(post);
        logger.info("Created post {}", saved.getId());
        return mapToResponse(saved);
    }

    public PostResponse updatePost(Long id, PostRequest request) {
        Post post = findPost(id);
        Category category = findCategory(request.getCategoryId());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(request.getAuthor());
        post.setCategory(category);
        return mapToResponse(postRepository.save(post));
    }

    public void deletePost(Long id) {
        Post post = findPost(id);
        postRepository.delete(post);
        logger.info("Deleted post {}", id);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return postRepository.findByCategoryId(categoryId).stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getPostsByAuthor(String author, Pageable pageable) {
        return postRepository.findByAuthorContainingIgnoreCase(author, pageable).map(this::mapToResponse);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private PostResponse mapToResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCategory().getId(),
                post.getCategory().getName(),
                post.getComments().size(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
