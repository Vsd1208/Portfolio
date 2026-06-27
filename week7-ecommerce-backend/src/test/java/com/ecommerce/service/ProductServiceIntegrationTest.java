package com.ecommerce.service;

import com.ecommerce.model.dto.ProductRequest;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductServiceIntegrationTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void updateWithoutActiveFlagPreservesCurrentState() {
        productService.deactivate(1L);

        var response = productService.update(1L, new ProductRequest(
                "Wireless Headphones",
                "Updated description",
                new BigDecimal("99.99"),
                100,
                1L,
                null,
                null
        ));

        assertThat(response.active()).isFalse();
        assertThat(productRepository.findById(1L).orElseThrow().isActive()).isFalse();
    }
}
