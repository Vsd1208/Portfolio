package com.erp.api;

import com.erp.api.dto.ErpDtos.DashboardSummary;
import com.erp.api.dto.ErpDtos.TopProduct;
import com.erp.domain.Product;
import com.erp.service.ErpService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasAnyRole('ADMIN','ACCOUNTANT','INVENTORY_MANAGER','PURCHASE_MANAGER')")
public class DashboardController {
  private final ErpService erp;

  public DashboardController(ErpService erp) {
    this.erp = erp;
  }

  @GetMapping("/sales-summary")
  DashboardSummary salesSummary() {
    return erp.dashboard();
  }

  @GetMapping("/purchase-summary")
  DashboardSummary purchaseSummary() {
    return erp.dashboard();
  }

  @GetMapping("/stock-alerts")
  List<Product> stockAlerts() {
    return erp.stockAlerts();
  }

  @GetMapping("/top-products")
  List<TopProduct> topProducts() {
    return erp.topProducts();
  }
}
