package com.erp.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Invoice {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(optional = false)
  private BusinessPartner customer;
  @OneToOne(optional = false)
  private SalesOrder salesOrder;
  private BigDecimal tax = BigDecimal.ZERO;
  private BigDecimal totalPayable = BigDecimal.ZERO;
  @Enumerated(EnumType.STRING)
  private InvoiceStatus status = InvoiceStatus.UNPAID;
  private LocalDate invoiceDate;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public BusinessPartner getCustomer() { return customer; }
  public void setCustomer(BusinessPartner customer) { this.customer = customer; }
  public SalesOrder getSalesOrder() { return salesOrder; }
  public void setSalesOrder(SalesOrder salesOrder) { this.salesOrder = salesOrder; }
  public BigDecimal getTax() { return tax; }
  public void setTax(BigDecimal tax) { this.tax = tax; }
  public BigDecimal getTotalPayable() { return totalPayable; }
  public void setTotalPayable(BigDecimal totalPayable) { this.totalPayable = totalPayable; }
  public InvoiceStatus getStatus() { return status; }
  public void setStatus(InvoiceStatus status) { this.status = status; }
  public LocalDate getInvoiceDate() { return invoiceDate; }
  public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }
}
