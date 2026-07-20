package com.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class GrnItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @JsonIgnore
  @ManyToOne(optional = false)
  private Grn grn;
  @ManyToOne(optional = false)
  private Product product;
  private int quantityReceived;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Grn getGrn() { return grn; }
  public void setGrn(Grn grn) { this.grn = grn; }
  public Product getProduct() { return product; }
  public void setProduct(Product product) { this.product = product; }
  public int getQuantityReceived() { return quantityReceived; }
  public void setQuantityReceived(int quantityReceived) { this.quantityReceived = quantityReceived; }
}
