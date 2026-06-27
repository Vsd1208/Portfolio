INSERT INTO users (email, password, name, role, created_at, updated_at) VALUES
('admin@example.com', '$2a$10$HltxWdcMwmxgdJMZiMB6U.frVh9ZOmOsykCkULztzcwDjPE0ulhR.', 'Store Admin', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('customer@example.com', '$2a$10$HltxWdcMwmxgdJMZiMB6U.frVh9ZOmOsykCkULztzcwDjPE0ulhR.', 'Demo Customer', 'CUSTOMER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO categories (name, description, parent_category_id, created_at, updated_at) VALUES
('Electronics', 'Devices and accessories', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Home Office', 'Products for productive workspaces', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Books', 'Technical and professional books', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO products (name, description, price, stock, category_id, image_url, active, version, created_at, updated_at) VALUES
('Wireless Headphones', 'Noise-isolating over-ear headphones', 99.99, 100, 1, NULL, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USB-C Cable', 'Durable two-meter charging cable', 19.99, 50, 1, NULL, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mechanical Keyboard', 'Compact mechanical keyboard', 79.50, 25, 2, NULL, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Spring Data in Practice', 'A practical database integration guide', 42.00, 30, 3, NULL, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
