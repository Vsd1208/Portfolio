package com.erp.repository;

import com.erp.domain.PurchaseOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
  List<PurchaseOrder> findByExpectedDeliveryDateBetween(java.time.LocalDate start, java.time.LocalDate end);
}
