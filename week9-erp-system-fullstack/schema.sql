CREATE TABLE user_account (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL
);

CREATE TABLE product (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  sku VARCHAR(255) UNIQUE NOT NULL,
  category VARCHAR(255) NOT NULL,
  unit_price NUMERIC(12, 2) NOT NULL,
  current_stock INTEGER NOT NULL,
  reorder_level INTEGER NOT NULL
);

CREATE TABLE business_partner (
  id BIGSERIAL PRIMARY KEY,
  type VARCHAR(20) NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone VARCHAR(50) NOT NULL,
  address TEXT NOT NULL,
  gstin VARCHAR(50)
);

CREATE TABLE sales_order (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT NOT NULL REFERENCES business_partner(id),
  order_date DATE NOT NULL,
  status VARCHAR(30) NOT NULL,
  total_amount NUMERIC(12, 2) NOT NULL
);

CREATE TABLE sales_order_item (
  id BIGSERIAL PRIMARY KEY,
  sales_order_id BIGINT NOT NULL REFERENCES sales_order(id),
  product_id BIGINT NOT NULL REFERENCES product(id),
  quantity INTEGER NOT NULL,
  unit_price NUMERIC(12, 2) NOT NULL,
  line_total NUMERIC(12, 2) NOT NULL
);

CREATE TABLE purchase_order (
  id BIGSERIAL PRIMARY KEY,
  supplier_id BIGINT NOT NULL REFERENCES business_partner(id),
  expected_delivery_date DATE NOT NULL,
  status VARCHAR(30) NOT NULL,
  total_amount NUMERIC(12, 2) NOT NULL
);

CREATE TABLE purchase_order_item (
  id BIGSERIAL PRIMARY KEY,
  purchase_order_id BIGINT NOT NULL REFERENCES purchase_order(id),
  product_id BIGINT NOT NULL REFERENCES product(id),
  quantity INTEGER NOT NULL,
  unit_price NUMERIC(12, 2) NOT NULL,
  line_total NUMERIC(12, 2) NOT NULL
);

CREATE TABLE grn (
  id BIGSERIAL PRIMARY KEY,
  supplier_id BIGINT NOT NULL REFERENCES business_partner(id),
  purchase_order_id BIGINT REFERENCES purchase_order(id),
  received_date DATE NOT NULL
);

CREATE TABLE grn_item (
  id BIGSERIAL PRIMARY KEY,
  grn_id BIGINT NOT NULL REFERENCES grn(id),
  product_id BIGINT NOT NULL REFERENCES product(id),
  quantity_received INTEGER NOT NULL
);

CREATE TABLE invoice (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT NOT NULL REFERENCES business_partner(id),
  sales_order_id BIGINT UNIQUE NOT NULL REFERENCES sales_order(id),
  tax NUMERIC(12, 2) NOT NULL,
  total_payable NUMERIC(12, 2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  invoice_date DATE NOT NULL
);
