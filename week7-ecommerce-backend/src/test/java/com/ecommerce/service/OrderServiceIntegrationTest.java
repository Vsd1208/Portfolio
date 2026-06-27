package com.ecommerce.service;

import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.model.dto.OrderItemRequest;
import com.ecommerce.model.dto.OrderRequest;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceIntegrationTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createsOrderAndDeductsStockInOneTransaction() {
        int stockBefore = productRepository.findById(1L).orElseThrow().getStock();

        var response = orderService.create(new OrderRequest(
                2L,
                List.of(new OrderItemRequest(1L, 2)),
                "123 Main Street"
        ));

        assertThat(response.totalAmount()).isEqualByComparingTo("199.98");
        assertThat(productRepository.findById(1L).orElseThrow().getStock()).isEqualTo(stockBefore - 2);
    }

    @Test
    void rollsBackOrderWhenStockIsInsufficient() {
        int stockBefore = productRepository.findById(2L).orElseThrow().getStock();

        assertThatThrownBy(() -> orderService.create(new OrderRequest(
                2L,
                List.of(new OrderItemRequest(2L, stockBefore + 1)),
                "123 Main Street"
        ))).isInstanceOf(InsufficientStockException.class);

        assertThat(productRepository.findById(2L).orElseThrow().getStock()).isEqualTo(stockBefore);
    }
}
