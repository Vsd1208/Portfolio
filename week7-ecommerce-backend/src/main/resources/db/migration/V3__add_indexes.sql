CREATE INDEX idx_product_name ON products(name);
CREATE INDEX idx_product_category ON products(category_id);
CREATE INDEX idx_product_active_price ON products(active, price);
CREATE INDEX idx_order_user_created ON orders(user_id, created_at);
CREATE INDEX idx_order_status ON orders(status);
CREATE INDEX idx_order_item_order ON order_items(order_id);
CREATE INDEX idx_payment_status ON payments(status);
