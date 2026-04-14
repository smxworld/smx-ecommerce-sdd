-- Seed: 10 products across 3 categories — active only with dev profile
-- Fixed UUIDs for cross-references between seed scripts

INSERT INTO smx_catalog.products (id, name, description, price, category, average_rating, review_count, search_score, created_at, updated_at) VALUES
('aaaaaaaa-0001-0000-0000-000000000001', 'Smartphone XPro',       'Next-generation smartphone with a 6.7" AMOLED display, 108MP camera, and 5000mAh battery.',                    699.00,  'Electronics', 4.5, 3, 0.9, NOW(), NOW()),
('aaaaaaaa-0002-0000-0000-000000000002', 'Laptop UltraSlim',      'Ultra-light 14" laptop with octa-core processor, 16GB RAM, 512GB SSD, and up to 12 hours of battery life.', 1299.00, 'Electronics', 4.2, 2, 0.8, NOW(), NOW()),
('aaaaaaaa-0003-0000-0000-000000000003', 'Wireless Headphones',   'Over-ear headphones with active noise cancellation, Bluetooth 5.3, and 30-hour battery life.',               149.00,  'Electronics', 0.0, 0, 0.5, NOW(), NOW()),
('aaaaaaaa-0004-0000-0000-000000000004', 'Mechanical Keyboard',   'TKL mechanical keyboard with blue switches, RGB backlight, and detachable USB-C cable.',                      89.00,   'Electronics', 0.0, 0, 0.4, NOW(), NOW()),
('aaaaaaaa-0005-0000-0000-000000000005', 'Premium T-Shirt',       'Organic cotton 200gsm t-shirt with a slim fit, available in 8 colors.',                                     29.00,   'Clothing',    0.0, 0, 0.3, NOW(), NOW()),
('aaaaaaaa-0006-0000-0000-000000000006', 'Slim Fit Jeans',        'Stretch denim jeans made of 98% cotton and 2% elastane with a slim fit and stone-washed finish.',          59.00,   'Clothing',    0.0, 0, 0.3, NOW(), NOW()),
('aaaaaaaa-0007-0000-0000-000000000007', 'Winter Jacket',         'Padded winter jacket with 90% goose down filling, waterproof shell, and removable hood.',                  199.00,  'Clothing',    0.0, 0, 0.4, NOW(), NOW()),
('aaaaaaaa-0008-0000-0000-000000000008', 'Desk Lamp',             'LED desk lamp with adjustable 2700-6500K color temperature, touch dimmer, and USB port.',                 49.00,   'Home',        0.0, 0, 0.2, NOW(), NOW()),
('aaaaaaaa-0009-0000-0000-000000000009', 'Cushion Set',           'Set of 2 hypoallergenic microfiber cushions with 400-thread-count Egyptian cotton covers, 50x70cm.',     39.00,   'Home',        0.0, 0, 0.2, NOW(), NOW()),
('aaaaaaaa-000a-0000-0000-00000000000a', 'Modern Rug',           'Hand-woven wool rug with geometric pattern, 160x230cm.',                                                   129.00,  'Home',        0.0, 0, 0.3, NOW(), NOW());
