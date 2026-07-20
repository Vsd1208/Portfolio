package com.erp.api;

import com.erp.api.dto.ErpDtos.PurchaseOrderRequest;
import com.erp.api.dto.ErpDtos.StatusRequest;
import com.erp.domain.PurchaseOrder;
import com.erp.repository.PurchaseOrderRepository;
import com.erp.service.ErpService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {
  private final PurchaseOrderRepository purchaseOrders;
  private final ErpService erp;

  public PurchaseOrderController(PurchaseOrderRepository purchaseOrders, ErpService erp) {
    this.purchaseOrders = purchaseOrders;
    this.erp = erp;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','PURCHASE_MANAGER','INVENTORY_MANAGER')")
  List<PurchaseOrder> all() {
    return purchaseOrders.findAll();
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','PURCHASE_MANAGER')")
  PurchaseOrder create(@Valid @RequestBody PurchaseOrderRequest request) {
    return erp.createPurchaseOrder(request);
  }

  @PutMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('ADMIN','PURCHASE_MANAGER')")
  PurchaseOrder status(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
    return erp.updatePurchaseStatus(id, request.status());
  }
}
