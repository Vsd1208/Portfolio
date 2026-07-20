package com.erp.api;

import com.erp.api.dto.ErpDtos.SalesOrderRequest;
import com.erp.api.dto.ErpDtos.StatusRequest;
import com.erp.domain.SalesOrder;
import com.erp.repository.SalesOrderRepository;
import com.erp.service.ErpService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales-orders")
public class SalesOrderController {
  private final SalesOrderRepository salesOrders;
  private final ErpService erp;

  public SalesOrderController(SalesOrderRepository salesOrders, ErpService erp) {
    this.salesOrders = salesOrders;
    this.erp = erp;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','SALES_EXECUTIVE','ACCOUNTANT')")
  List<SalesOrder> all() {
    return salesOrders.findAll();
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','SALES_EXECUTIVE')")
  SalesOrder create(@Valid @RequestBody SalesOrderRequest request) {
    return erp.createSalesOrder(request);
  }

  @PutMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('ADMIN','SALES_EXECUTIVE')")
  SalesOrder status(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
    return erp.updateSalesStatus(id, request.status());
  }
}
