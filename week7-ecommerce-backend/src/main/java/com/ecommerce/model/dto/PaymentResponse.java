package com.ecommerce.model.dto;

import com.ecommerce.model.enums.PaymentMethod;
import com.ecommerce.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        String transactionId,
        LocalDateTime createdAt
) {
}
