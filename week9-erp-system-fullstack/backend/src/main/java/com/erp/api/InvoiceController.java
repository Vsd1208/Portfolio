package com.erp.api;

import com.erp.api.dto.ErpDtos.InvoiceRequest;
import com.erp.domain.Invoice;
import com.erp.repository.InvoiceRepository;
import com.erp.service.ErpService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@PreAuthorize("hasAnyRole('ADMIN','SALES_EXECUTIVE','ACCOUNTANT')")
public class InvoiceController {
  private final InvoiceRepository invoices;
  private final ErpService erp;

  public InvoiceController(InvoiceRepository invoices, ErpService erp) {
    this.invoices = invoices;
    this.erp = erp;
  }

  @GetMapping
  List<Invoice> all() {
    return invoices.findAll();
  }

  @PostMapping
  Invoice create(@Valid @RequestBody InvoiceRequest request) {
    return erp.createInvoice(request);
  }

  @GetMapping("/{id}/pdf")
  ResponseEntity<byte[]> pdf(@PathVariable Long id) {
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + id + ".pdf")
        .contentType(MediaType.APPLICATION_PDF)
        .body(erp.invoicePdf(id));
  }
}
