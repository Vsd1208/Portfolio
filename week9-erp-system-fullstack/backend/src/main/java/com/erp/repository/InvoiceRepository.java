package com.erp.repository;

import com.erp.domain.Invoice;
import com.erp.domain.InvoiceStatus;
import com.erp.domain.SalesOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
  Optional<Invoice> findBySalesOrder(SalesOrder salesOrder);
  long countByStatus(InvoiceStatus status);
}
