package com.erp.repository;

import com.erp.domain.OrderStatus;
import com.erp.domain.SalesOrder;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
  List<SalesOrder> findByOrderDateBetween(LocalDate start, LocalDate end);
  long countByStatus(OrderStatus status);
}
