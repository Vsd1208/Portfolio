package com.erp.api;

import com.erp.api.dto.ErpDtos.GrnRequest;
import com.erp.domain.Grn;
import com.erp.repository.GrnRepository;
import com.erp.service.ErpService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grns")
@PreAuthorize("hasAnyRole('ADMIN','PURCHASE_MANAGER','INVENTORY_MANAGER')")
public class GrnController {
  private final GrnRepository grns;
  private final ErpService erp;

  public GrnController(GrnRepository grns, ErpService erp) {
    this.grns = grns;
    this.erp = erp;
  }

  @GetMapping
  List<Grn> all() {
    return grns.findAll();
  }

  @PostMapping
  Grn create(@Valid @RequestBody GrnRequest request) {
    return erp.createGrn(request);
  }
}
