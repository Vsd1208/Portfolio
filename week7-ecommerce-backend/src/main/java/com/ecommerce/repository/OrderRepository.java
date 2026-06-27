package com.ecommerce.repository;

import com.ecommerce.model.entity.Order;
import com.ecommerce.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserId(Long userId, Pageable pageable);
    List<Order> findByStatus(OrderStatus status);

    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.product"})
    @Query("select distinct o from Order o where o.id = :id")
    Optional<Order> findDetailsById(@Param("id") Long id);

    @Query("select o from Order o where o.createdAt between :start and :end")
    List<Order> findOrdersBetweenDates(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    @Query(value = """
            select cast(o.created_at as date) as order_date,
                   count(*) as total_orders,
                   sum(o.total_amount) as total_revenue
            from orders o
            where o.created_at >= :startDate
            group by cast(o.created_at as date)
            order by order_date desc
            """, nativeQuery = true)
    List<Object[]> getDailyOrderReport(@Param("startDate") LocalDateTime startDate);
}
