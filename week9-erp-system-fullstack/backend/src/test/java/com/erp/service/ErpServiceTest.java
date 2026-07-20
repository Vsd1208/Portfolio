package com.erp.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.erp.api.dto.ErpDtos.GrnRequest;
import com.erp.api.dto.ErpDtos.LineItemRequest;
import com.erp.domain.PartnerType;
import com.erp.repository.BusinessPartnerRepository;
import com.erp.repository.ProductRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ErpServiceTest {
  @Autowired ErpService erp;
  @Autowired ProductRepository products;
  @Autowired BusinessPartnerRepository partners;

  @Test
  void grnIncrementsStock() {
    var product = products.findAll().get(0);
    var supplier = partners.findByType(PartnerType.SUPPLIER).get(0);
    var before = product.getCurrentStock();

    erp.createGrn(new GrnRequest(null, supplier.getId(), LocalDate.now(), List.of(new LineItemRequest(product.getId(), 7))));

    assertThat(products.findById(product.getId()).orElseThrow().getCurrentStock()).isEqualTo(before + 7);
  }
}
