package com.ecommerce.repository;

import com.ecommerce.model.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
            select p from Product p
            join fetch p.category
            where p.active = true
              and (:categoryId is null or p.category.id = :categoryId)
              and (:search is null or lower(p.name) like lower(concat('%', :search, '%')))
              and (:maxPrice is null or p.price <= :maxPrice)
            """)
    Page<Product> search(@Param("categoryId") Long categoryId,
                         @Param("search") String search,
                         @Param("maxPrice") BigDecimal maxPrice,
                         Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);
}
