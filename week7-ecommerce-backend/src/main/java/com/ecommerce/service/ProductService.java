package com.ecommerce.service;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.dto.ProductRequest;
import com.ecommerce.model.dto.ProductResponse;
import com.ecommerce.model.entity.Category;
import com.ecommerce.model.entity.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> search(Long categoryId, String search, BigDecimal maxPrice, Pageable pageable) {
        String normalized = search == null || search.isBlank() ? null : search.trim();
        return productRepository.search(categoryId, normalized, maxPrice, pageable).map(this::toResponse);
    }

    @Cacheable(cacheNames = "products", key = "#id")
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return toResponse(findProduct(id));
    }

    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        apply(product, request, true);
        return toResponse(productRepository.save(product));
    }

    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findProduct(id);
        apply(product, request, false);
        return toResponse(productRepository.save(product));
    }

    @CacheEvict(cacheNames = "products", allEntries = true)
    public void deactivate(Long id) {
        Product product = findProduct(id);
        product.setActive(false);
        productRepository.save(product);
    }

    private void apply(Product product, ProductRequest request, boolean creating) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.categoryId()));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setCategory(category);
        product.setImageUrl(request.imageUrl());
        if (request.active() != null) {
            product.setActive(request.active());
        } else if (creating) {
            product.setActive(true);
        }
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStock(), product.getCategory().getId(),
                product.getCategory().getName(), product.getImageUrl(), product.isActive(),
                product.getCreatedAt(), product.getUpdatedAt());
    }
}
