package com.ecommerce.model.dto;

import com.ecommerce.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        Long userId,
        List<Item> items,
        BigDecimal totalAmount,
        OrderStatus status,
        String shippingAddress,
        LocalDateTime createdAt
) {
    public record Item(Long productId, String productName, Integer quantity,
                       BigDecimal unitPrice, BigDecimal subtotal) {
    }
}
