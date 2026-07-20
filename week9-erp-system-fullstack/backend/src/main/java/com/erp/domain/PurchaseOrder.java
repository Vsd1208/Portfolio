package com.erp.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PurchaseOrder {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(optional = false)
  private BusinessPartner supplier;
  private LocalDate expectedDeliveryDate;
  @Enumerated(EnumType.STRING)
  private OrderStatus status = OrderStatus.ORDERED;
  private BigDecimal totalAmount = BigDecimal.ZERO;
  @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PurchaseOrderItem> items = new ArrayList<>();

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public BusinessPartner getSupplier() { return supplier; }
  public void setSupplier(BusinessPartner supplier) { this.supplier = supplier; }
  public LocalDate getExpectedDeliveryDate() { return expectedDeliveryDate; }
  public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }
  public OrderStatus getStatus() { return status; }
  public void setStatus(OrderStatus status) { this.status = status; }
  public BigDecimal getTotalAmount() { return totalAmount; }
  public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
  public List<PurchaseOrderItem> getItems() { return items; }
  public void setItems(List<PurchaseOrderItem> items) { this.items = items; }
}
