package com.erp.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SalesOrder {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(optional = false)
  private BusinessPartner customer;
  private LocalDate orderDate;
  @Enumerated(EnumType.STRING)
  private OrderStatus status = OrderStatus.PENDING;
  private BigDecimal totalAmount = BigDecimal.ZERO;
  @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SalesOrderItem> items = new ArrayList<>();

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public BusinessPartner getCustomer() { return customer; }
  public void setCustomer(BusinessPartner customer) { this.customer = customer; }
  public LocalDate getOrderDate() { return orderDate; }
  public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
  public OrderStatus getStatus() { return status; }
  public void setStatus(OrderStatus status) { this.status = status; }
  public BigDecimal getTotalAmount() { return totalAmount; }
  public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
  public List<SalesOrderItem> getItems() { return items; }
  public void setItems(List<SalesOrderItem> items) { this.items = items; }
}
