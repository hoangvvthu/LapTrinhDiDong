USE shop_api;

-- Xóa dữ liệu cũ nếu có
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
SET FOREIGN_KEY_CHECKS = 1;

-- Thêm danh mục
INSERT INTO categories (id, name) VALUES (1, 'Dien thoai');
INSERT INTO categories (id, name) VALUES (2, 'Laptop');
INSERT INTO categories (id, name) VALUES (3, 'Phu kien');

-- Thêm sản phẩm mẫu
INSERT INTO products (name, price, quantity, sold, create_date, category_id) VALUES 
('iPhone 15 Pro Max', 1400, 50, 500, NOW(), 1),
('Samsung S24 Ultra', 1300, 40, 450, NOW(), 1),
('MacBook Pro M3', 2500, 20, 300, '2024-01-15', 2),
('Tai nghe AirPods 3', 150, 100, 200, NOW(), 3),
('Xiaomi 14', 800, 60, 150, NOW(), 1),
('Dell XPS 15', 1800, 15, 90, '2023-12-20', 2),
('Asus ROG Strix', 1600, 10, 80, NOW(), 2),
('iPad Air 5', 600, 30, 70, '2023-11-10', 1),
('Chuot Logitech G502', 50, 200, 60, NOW(), 3),
('Ban phim Co', 80, 100, 55, NOW(), 3),
('Op lung iPhone', 10, 500, 5, NOW(), 3),
('Cu sac Samsung', 20, 200, 2, NOW(), 3);