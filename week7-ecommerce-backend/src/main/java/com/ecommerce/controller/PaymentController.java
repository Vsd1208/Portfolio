package com.ecommerce.controller;

import com.ecommerce.model.dto.PaymentRequest;
import com.ecommerce.model.dto.PaymentResponse;
import com.ecommerce.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }

    @PostMapping
    public ResponseEntity<PaymentResponse> process(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.process(request));
    }

    @GetMapping("/order/{orderId}")
    public PaymentResponse byOrder(@PathVariable Long orderId) {
        return paymentService.getByOrder(orderId);
    }
}
