package com.erp.repository;

import com.erp.domain.BusinessPartner;
import com.erp.domain.PartnerType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessPartnerRepository extends JpaRepository<BusinessPartner, Long> {
  List<BusinessPartner> findByType(PartnerType type);
}
