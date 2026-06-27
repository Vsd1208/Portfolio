package com.ecommerce.model.dto;

import com.ecommerce.model.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(@NotNull Long orderId, @NotNull PaymentMethod method) {
}
