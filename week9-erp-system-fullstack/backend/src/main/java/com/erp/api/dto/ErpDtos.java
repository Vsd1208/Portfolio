package com.erp.api.dto;

import com.erp.domain.InvoiceStatus;
import com.erp.domain.OrderStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class ErpDtos {
  private ErpDtos() {}

  public record PartnerRequest(@NotBlank String name, @Email @NotBlank String email, @NotBlank String phone, @NotBlank String address, String gstin) {}
  public record LineItemRequest(@NotNull Long productId, @Min(1) int quantity) {}
  public record SalesOrderRequest(@NotNull Long customerId, @NotNull LocalDate orderDate, @NotEmpty List<LineItemRequest> items) {}
  public record PurchaseOrderRequest(@NotNull Long supplierId, @NotNull LocalDate expectedDeliveryDate, @NotEmpty List<LineItemRequest> items) {}
  public record StatusRequest(@NotNull OrderStatus status) {}
  public record GrnRequest(Long purchaseOrderId, @NotNull Long supplierId, @NotNull LocalDate receivedDate, @NotEmpty List<LineItemRequest> items) {}
  public record InvoiceRequest(@NotNull Long salesOrderId, @NotNull @DecimalMin("0.0") BigDecimal tax, InvoiceStatus status) {}
  public record ItemResponse(Long productId, String productName, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {}
  public record DashboardSummary(BigDecimal totalSalesThisMonth, BigDecimal totalPurchasesThisMonth, long pendingInvoices, long pendingSalesOrders) {}
  public record TopProduct(String productName, int quantitySold) {}
}
