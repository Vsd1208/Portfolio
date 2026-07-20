package com.erp.config;

import com.erp.domain.*;
import com.erp.repository.*;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
  @Bean
  CommandLineRunner seed(UserAccountRepository users, ProductRepository products, BusinessPartnerRepository partners, PasswordEncoder encoder) {
    return args -> {
      if (users.count() == 0) {
        addUser(users, encoder, "admin", "admin@erp.local", Role.ADMIN);
        addUser(users, encoder, "sales", "sales@erp.local", Role.SALES_EXECUTIVE);
        addUser(users, encoder, "purchase", "purchase@erp.local", Role.PURCHASE_MANAGER);
        addUser(users, encoder, "inventory", "inventory@erp.local", Role.INVENTORY_MANAGER);
        addUser(users, encoder, "accounts", "accounts@erp.local", Role.ACCOUNTANT);
      }
      if (products.count() == 0) {
        addProduct(products, "USB-C Docking Station", "SKU-DOCK-100", "Electronics", "6499.00", 32, 8);
        addProduct(products, "Barcode Scanner", "SKU-SCAN-220", "Warehouse", "3899.00", 5, 6);
        addProduct(products, "Thermal Label Roll", "SKU-LBL-500", "Consumables", "399.00", 120, 40);
      }
      if (partners.count() == 0) {
        addPartner(partners, PartnerType.CUSTOMER, "Acme Retail Pvt Ltd", "orders@acme.example", "9876543210", "Mumbai", "27ABCDE1234F1Z5");
        addPartner(partners, PartnerType.CUSTOMER, "Northline Traders", "buy@northline.example", "9876501234", "Delhi", null);
        addPartner(partners, PartnerType.SUPPLIER, "Prime Supply Co", "sales@prime.example", "9000011111", "Pune", "27AAACP1111F1Z7");
        addPartner(partners, PartnerType.SUPPLIER, "Global Components", "desk@global.example", "9000022222", "Bengaluru", null);
      }
    };
  }

  private void addUser(UserAccountRepository users, PasswordEncoder encoder, String username, String email, Role role) {
    var user = new UserAccount();
    user.setUsername(username);
    user.setEmail(email);
    user.setRole(role);
    user.setPasswordHash(encoder.encode("password"));
    users.save(user);
  }

  private void addProduct(ProductRepository products, String name, String sku, String category, String price, int stock, int reorder) {
    var product = new Product();
    product.setName(name);
    product.setSku(sku);
    product.setCategory(category);
    product.setUnitPrice(new BigDecimal(price));
    product.setCurrentStock(stock);
    product.setReorderLevel(reorder);
    products.save(product);
  }

  private void addPartner(BusinessPartnerRepository partners, PartnerType type, String name, String email, String phone, String address, String gstin) {
    var partner = new BusinessPartner();
    partner.setType(type);
    partner.setName(name);
    partner.setEmail(email);
    partner.setPhone(phone);
    partner.setAddress(address);
    partner.setGstin(gstin);
    partners.save(partner);
  }
}
