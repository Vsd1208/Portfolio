package com.erp.api;

import com.erp.api.dto.ErpDtos.PartnerRequest;
import com.erp.domain.BusinessPartner;
import com.erp.domain.PartnerType;
import com.erp.service.ErpService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class PartnerController {
  private final ErpService erp;

  public PartnerController(ErpService erp) {
    this.erp = erp;
  }

  @GetMapping("/api/customers")
  @PreAuthorize("hasAnyRole('ADMIN','SALES_EXECUTIVE','ACCOUNTANT')")
  List<BusinessPartner> customers() {
    return erp.partners(PartnerType.CUSTOMER);
  }

  @PostMapping("/api/customers")
  @PreAuthorize("hasAnyRole('ADMIN','SALES_EXECUTIVE')")
  BusinessPartner createCustomer(@Valid @RequestBody PartnerRequest request) {
    return erp.createPartner(request, PartnerType.CUSTOMER);
  }

  @GetMapping("/api/suppliers")
  @PreAuthorize("hasAnyRole('ADMIN','PURCHASE_MANAGER','INVENTORY_MANAGER')")
  List<BusinessPartner> suppliers() {
    return erp.partners(PartnerType.SUPPLIER);
  }

  @PostMapping("/api/suppliers")
  @PreAuthorize("hasAnyRole('ADMIN','PURCHASE_MANAGER')")
  BusinessPartner createSupplier(@Valid @RequestBody PartnerRequest request) {
    return erp.createPartner(request, PartnerType.SUPPLIER);
  }
}
