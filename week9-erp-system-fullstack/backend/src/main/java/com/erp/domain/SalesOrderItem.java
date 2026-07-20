package com.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class SalesOrderItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @JsonIgnore
  @ManyToOne(optional = false)
  private SalesOrder salesOrder;
  @ManyToOne(optional = false)
  private Product product;
  private int quantity;
  private BigDecimal unitPrice;
  private BigDecimal lineTotal;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public SalesOrder getSalesOrder() { return salesOrder; }
  public void setSalesOrder(SalesOrder salesOrder) { this.salesOrder = salesOrder; }
  public Product getProduct() { return product; }
  public void setProduct(Product product) { this.product = product; }
  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
  public BigDecimal getUnitPrice() { return unitPrice; }
  public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
  public BigDecimal getLineTotal() { return lineTotal; }
  public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}
