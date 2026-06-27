package com.ecommerce.controller;

import com.ecommerce.model.dto.OrderRequest;
import com.ecommerce.model.dto.OrderResponse;
import com.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) { return orderService.getById(id); }

    @GetMapping
    public Page<OrderResponse> byUser(@RequestParam Long userId,
                                      @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return orderService.getUserOrders(userId, pageable);
    }

    @PutMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable Long id) { return orderService.cancel(id); }

    @GetMapping("/report/daily")
    public List<Object[]> report(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {
        return orderService.dailyReport(startDate);
    }
}
