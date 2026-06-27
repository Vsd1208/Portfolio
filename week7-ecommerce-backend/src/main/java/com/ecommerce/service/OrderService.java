package com.ecommerce.service;

import com.ecommerce.exception.ConflictException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.dto.OrderItemRequest;
import com.ecommerce.model.dto.OrderRequest;
import com.ecommerce.model.dto.OrderResponse;
import com.ecommerce.model.entity.Order;
import com.ecommerce.model.entity.OrderItem;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.entity.User;
import com.ecommerce.model.enums.OrderStatus;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public OrderResponse create(OrderRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setShippingAddress(request.shippingAddress());

        for (OrderItemRequest requestedItem : request.items()) {
            Product product = productRepository.findByIdForUpdate(requestedItem.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found: " + requestedItem.productId()));
            if (!product.isActive()) {
                throw new ConflictException("Product is not available: " + product.getName());
            }
            product.decreaseStock(requestedItem.quantity());

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(requestedItem.quantity());
            item.setUnitPrice(product.getPrice());
            order.addOrderItem(item);
        }

        order.setTotalAmount(order.calculateTotal());
        Order saved = orderRepository.save(order);
        logger.info("Created order {} with {} items", saved.getOrderNumber(), saved.getOrderItems().size());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        return toResponse(findDetailedOrder(id));
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrders(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }
        return orderRepository.findByUserId(userId, pageable).map(this::toResponse);
    }

    public OrderResponse cancel(Long id) {
        Order order = findDetailedOrder(id);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ConflictException("Only pending orders can be cancelled");
        }
        order.getOrderItems().forEach(item -> item.getProduct().increaseStock(item.getQuantity()));
        order.setStatus(OrderStatus.CANCELLED);
        return toResponse(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<Object[]> dailyReport(LocalDateTime startDate) {
        return orderRepository.getDailyOrderReport(startDate);
    }

    private Order findDetailedOrder(Long id) {
        return orderRepository.findDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    private String generateOrderNumber() {
        return "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    OrderResponse toResponse(Order order) {
        List<OrderResponse.Item> items = order.getOrderItems().stream()
                .map(item -> new OrderResponse.Item(item.getProduct().getId(), item.getProduct().getName(),
                        item.getQuantity(), item.getUnitPrice(), item.getSubtotal()))
                .toList();
        return new OrderResponse(order.getId(), order.getOrderNumber(), order.getUser().getId(), items,
                order.getTotalAmount(), order.getStatus(), order.getShippingAddress(), order.getCreatedAt());
    }
}
