package com.erp.service;

import com.erp.api.dto.ErpDtos.*;
import com.erp.domain.*;
import com.erp.repository.*;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ErpService {
  private final ProductRepository products;
  private final BusinessPartnerRepository partners;
  private final SalesOrderRepository salesOrders;
  private final PurchaseOrderRepository purchaseOrders;
  private final GrnRepository grns;
  private final InvoiceRepository invoices;

  public ErpService(ProductRepository products, BusinessPartnerRepository partners, SalesOrderRepository salesOrders,
      PurchaseOrderRepository purchaseOrders, GrnRepository grns, InvoiceRepository invoices) {
    this.products = products;
    this.partners = partners;
    this.salesOrders = salesOrders;
    this.purchaseOrders = purchaseOrders;
    this.grns = grns;
    this.invoices = invoices;
  }

  public Product product(Long id) { return products.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found")); }
  public BusinessPartner partner(Long id) { return partners.findById(id).orElseThrow(() -> new EntityNotFoundException("Partner not found")); }
  public List<BusinessPartner> partners(PartnerType type) { return partners.findByType(type); }

  public BusinessPartner createPartner(PartnerRequest request, PartnerType type) {
    var partner = new BusinessPartner();
    partner.setType(type);
    partner.setName(request.name());
    partner.setEmail(request.email());
    partner.setPhone(request.phone());
    partner.setAddress(request.address());
    partner.setGstin(request.gstin());
    return partners.save(partner);
  }

  @Transactional
  public SalesOrder createSalesOrder(SalesOrderRequest request) {
    var customer = partner(request.customerId());
    if (customer.getType() != PartnerType.CUSTOMER) throw new IllegalArgumentException("Selected partner is not a customer");
    var order = new SalesOrder();
    order.setCustomer(customer);
    order.setOrderDate(request.orderDate());
    addSalesItems(order, request.items());
    return salesOrders.save(order);
  }

  @Transactional
  public SalesOrder updateSalesStatus(Long id, OrderStatus status) {
    if (status == OrderStatus.ORDERED || status == OrderStatus.RECEIVED) throw new IllegalArgumentException("Invalid sales order status");
    var order = salesOrders.findById(id).orElseThrow(() -> new EntityNotFoundException("Sales order not found"));
    order.setStatus(status);
    return order;
  }

  @Transactional
  public PurchaseOrder createPurchaseOrder(PurchaseOrderRequest request) {
    var supplier = partner(request.supplierId());
    if (supplier.getType() != PartnerType.SUPPLIER) throw new IllegalArgumentException("Selected partner is not a supplier");
    var order = new PurchaseOrder();
    order.setSupplier(supplier);
    order.setExpectedDeliveryDate(request.expectedDeliveryDate());
    addPurchaseItems(order, request.items());
    return purchaseOrders.save(order);
  }

  @Transactional
  public PurchaseOrder updatePurchaseStatus(Long id, OrderStatus status) {
    if (status != OrderStatus.ORDERED && status != OrderStatus.RECEIVED) throw new IllegalArgumentException("Invalid purchase order status");
    var order = purchaseOrders.findById(id).orElseThrow(() -> new EntityNotFoundException("Purchase order not found"));
    order.setStatus(status);
    return order;
  }

  @Transactional
  public Grn createGrn(GrnRequest request) {
    var supplier = partner(request.supplierId());
    if (supplier.getType() != PartnerType.SUPPLIER) throw new IllegalArgumentException("Selected partner is not a supplier");
    var grn = new Grn();
    grn.setSupplier(supplier);
    grn.setReceivedDate(request.receivedDate());
    if (request.purchaseOrderId() != null) {
      var po = purchaseOrders.findById(request.purchaseOrderId()).orElseThrow(() -> new EntityNotFoundException("Purchase order not found"));
      po.setStatus(OrderStatus.RECEIVED);
      grn.setPurchaseOrder(po);
    }
    for (var itemRequest : request.items()) {
      var product = product(itemRequest.productId());
      product.setCurrentStock(product.getCurrentStock() + itemRequest.quantity());
      var item = new GrnItem();
      item.setGrn(grn);
      item.setProduct(product);
      item.setQuantityReceived(itemRequest.quantity());
      grn.getItems().add(item);
    }
    return grns.save(grn);
  }

  @Transactional
  public Invoice createInvoice(InvoiceRequest request) {
    var order = salesOrders.findById(request.salesOrderId()).orElseThrow(() -> new EntityNotFoundException("Sales order not found"));
    if (order.getStatus() != OrderStatus.APPROVED && order.getStatus() != OrderStatus.DISPATCHED) {
      throw new IllegalArgumentException("Invoice can only be generated from an approved or dispatched sales order");
    }
    invoices.findBySalesOrder(order).ifPresent(existing -> { throw new IllegalArgumentException("Invoice already exists for this sales order"); });
    var invoice = new Invoice();
    invoice.setSalesOrder(order);
    invoice.setCustomer(order.getCustomer());
    invoice.setInvoiceDate(LocalDate.now());
    invoice.setTax(request.tax());
    invoice.setTotalPayable(order.getTotalAmount().add(request.tax()));
    invoice.setStatus(request.status() == null ? InvoiceStatus.UNPAID : request.status());
    return invoices.save(invoice);
  }

  public byte[] invoicePdf(Long id) {
    var invoice = invoices.findById(id).orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
    var text = "ERP Invoice #" + invoice.getId() + "\nCustomer: " + invoice.getCustomer().getName()
        + "\nSales Order: " + invoice.getSalesOrder().getId()
        + "\nTax: " + invoice.getTax()
        + "\nTotal Payable: " + invoice.getTotalPayable()
        + "\nStatus: " + invoice.getStatus();
    return text.getBytes(java.nio.charset.StandardCharsets.UTF_8);
  }

  public DashboardSummary dashboard() {
    var month = YearMonth.now();
    var start = month.atDay(1);
    var end = month.atEndOfMonth();
    var sales = salesOrders.findByOrderDateBetween(start, end).stream().map(SalesOrder::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    var purchases = purchaseOrders.findByExpectedDeliveryDateBetween(start, end).stream().map(PurchaseOrder::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    return new DashboardSummary(sales, purchases, invoices.countByStatus(InvoiceStatus.UNPAID), salesOrders.countByStatus(OrderStatus.PENDING));
  }

  public List<TopProduct> topProducts() {
    Map<String, Integer> totals = salesOrders.findAll().stream()
        .flatMap(order -> order.getItems().stream())
        .collect(Collectors.groupingBy(item -> item.getProduct().getName(), Collectors.summingInt(SalesOrderItem::getQuantity)));
    return totals.entrySet().stream()
        .map(entry -> new TopProduct(entry.getKey(), entry.getValue()))
        .sorted(Comparator.comparing(TopProduct::quantitySold).reversed())
        .limit(5)
        .toList();
  }

  public List<Product> stockAlerts() {
    return products.findAll().stream().filter(product -> product.getCurrentStock() <= product.getReorderLevel()).toList();
  }

  private void addSalesItems(SalesOrder order, List<LineItemRequest> requests) {
    var total = BigDecimal.ZERO;
    for (var request : requests) {
      var product = product(request.productId());
      if (product.getCurrentStock() < request.quantity()) throw new IllegalArgumentException("Insufficient stock for " + product.getName());
      product.setCurrentStock(product.getCurrentStock() - request.quantity());
      var line = product.getUnitPrice().multiply(BigDecimal.valueOf(request.quantity()));
      var item = new SalesOrderItem();
      item.setSalesOrder(order);
      item.setProduct(product);
      item.setQuantity(request.quantity());
      item.setUnitPrice(product.getUnitPrice());
      item.setLineTotal(line);
      order.getItems().add(item);
      total = total.add(line);
    }
    order.setTotalAmount(total);
  }

  private void addPurchaseItems(PurchaseOrder order, List<LineItemRequest> requests) {
    var total = BigDecimal.ZERO;
    for (var request : requests) {
      var product = product(request.productId());
      var line = product.getUnitPrice().multiply(BigDecimal.valueOf(request.quantity()));
      var item = new PurchaseOrderItem();
      item.setPurchaseOrder(order);
      item.setProduct(product);
      item.setQuantity(request.quantity());
      item.setUnitPrice(product.getUnitPrice());
      item.setLineTotal(line);
      order.getItems().add(item);
      total = total.add(line);
    }
    order.setTotalAmount(total);
  }
}
