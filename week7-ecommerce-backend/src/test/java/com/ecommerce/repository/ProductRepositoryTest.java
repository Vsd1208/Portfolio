package com.ecommerce.repository;

import com.ecommerce.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void filtersActiveProductsByCategoryAndPrice() {
        var products = productRepository.search(1L, null, new BigDecimal("50.00"), PageRequest.of(0, 10));

        assertThat(products.getContent())
                .extracting(Product::getName)
                .containsExactly("USB-C Cable");
    }
}
