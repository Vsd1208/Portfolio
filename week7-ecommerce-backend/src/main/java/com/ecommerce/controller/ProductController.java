package com.ecommerce.controller;

import com.ecommerce.model.dto.ProductRequest;
import com.ecommerce.model.dto.ProductResponse;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) { this.productService = productService; }

    @GetMapping
    public Page<ProductResponse> search(@RequestParam(required = false) Long categoryId,
                                        @RequestParam(required = false) String search,
                                        @RequestParam(required = false) BigDecimal maxPrice,
                                        @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return productService.search(categoryId, search, maxPrice, pageable);
    }

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable Long id) { return productService.getById(id); }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        productService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
