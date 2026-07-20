package com.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class Product {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank
  private String name;
  @NotBlank @Column(unique = true)
  private String sku;
  @NotBlank
  private String category;
  @NotNull @DecimalMin("0.0")
  private BigDecimal unitPrice;
  @Min(0)
  private int currentStock;
  @Min(0)
  private int reorderLevel;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getSku() { return sku; }
  public void setSku(String sku) { this.sku = sku; }
  public String getCategory() { return category; }
  public void setCategory(String category) { this.category = category; }
  public BigDecimal getUnitPrice() { return unitPrice; }
  public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
  public int getCurrentStock() { return currentStock; }
  public void setCurrentStock(int currentStock) { this.currentStock = currentStock; }
  public int getReorderLevel() { return reorderLevel; }
  public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }
}
