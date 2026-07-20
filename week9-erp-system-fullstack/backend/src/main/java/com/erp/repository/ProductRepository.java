package com.erp.repository;

import com.erp.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByCurrentStockLessThanEqual(int stock);
}
