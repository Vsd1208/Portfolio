package com.erp.api;

import com.erp.domain.Product;
import com.erp.repository.ProductRepository;
import com.erp.service.ErpService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductRepository products;
  private final ErpService erp;

  public ProductController(ProductRepository products, ErpService erp) {
    this.products = products;
    this.erp = erp;
  }

  @GetMapping
  List<Product> all() {
    return products.findAll();
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','PURCHASE_MANAGER')")
  Product create(@Valid @RequestBody Product product) {
    return products.save(product);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','PURCHASE_MANAGER')")
  Product update(@PathVariable Long id, @Valid @RequestBody Product request) {
    var product = erp.product(id);
    product.setName(request.getName());
    product.setSku(request.getSku());
    product.setCategory(request.getCategory());
    product.setUnitPrice(request.getUnitPrice());
    product.setCurrentStock(request.getCurrentStock());
    product.setReorderLevel(request.getReorderLevel());
    return products.save(product);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  void delete(@PathVariable Long id) {
    products.deleteById(id);
  }
}
