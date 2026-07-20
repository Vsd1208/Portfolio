package com.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Grn {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(optional = false)
  private BusinessPartner supplier;
  @ManyToOne
  private PurchaseOrder purchaseOrder;
  private LocalDate receivedDate;
  @OneToMany(mappedBy = "grn", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GrnItem> items = new ArrayList<>();

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public BusinessPartner getSupplier() { return supplier; }
  public void setSupplier(BusinessPartner supplier) { this.supplier = supplier; }
  public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }
  public void setPurchaseOrder(PurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; }
  public LocalDate getReceivedDate() { return receivedDate; }
  public void setReceivedDate(LocalDate receivedDate) { this.receivedDate = receivedDate; }
  public List<GrnItem> getItems() { return items; }
  public void setItems(List<GrnItem> items) { this.items = items; }
}
