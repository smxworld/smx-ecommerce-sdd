UPDATE smx_catalog.products
SET
    name = CASE id
        WHEN 'aaaaaaaa-0001-0000-0000-000000000001' THEN 'Smartphone XPro'
        WHEN 'aaaaaaaa-0002-0000-0000-000000000002' THEN 'Laptop UltraSlim'
        WHEN 'aaaaaaaa-0003-0000-0000-000000000003' THEN 'Wireless Headphones'
        WHEN 'aaaaaaaa-0004-0000-0000-000000000004' THEN 'Mechanical Keyboard'
        WHEN 'aaaaaaaa-0005-0000-0000-000000000005' THEN 'Premium T-Shirt'
        WHEN 'aaaaaaaa-0006-0000-0000-000000000006' THEN 'Slim Fit Jeans'
        WHEN 'aaaaaaaa-0007-0000-0000-000000000007' THEN 'Winter Jacket'
        WHEN 'aaaaaaaa-0008-0000-0000-000000000008' THEN 'Desk Lamp'
        WHEN 'aaaaaaaa-0009-0000-0000-000000000009' THEN 'Cushion Set'
        WHEN 'aaaaaaaa-000a-0000-0000-00000000000a' THEN 'Modern Rug'
        ELSE name
    END,
    description = CASE id
        WHEN 'aaaaaaaa-0001-0000-0000-000000000001' THEN 'Next-generation smartphone with a 6.7" AMOLED display, 108MP camera, and 5000mAh battery.'
        WHEN 'aaaaaaaa-0002-0000-0000-000000000002' THEN 'Ultra-light 14" laptop with octa-core processor, 16GB RAM, 512GB SSD, and up to 12 hours of battery life.'
        WHEN 'aaaaaaaa-0003-0000-0000-000000000003' THEN 'Over-ear headphones with active noise cancellation, Bluetooth 5.3, and 30-hour battery life.'
        WHEN 'aaaaaaaa-0004-0000-0000-000000000004' THEN 'TKL mechanical keyboard with blue switches, RGB backlight, and detachable USB-C cable.'
        WHEN 'aaaaaaaa-0005-0000-0000-000000000005' THEN 'Organic cotton 200gsm t-shirt with a slim fit, available in 8 colors.'
        WHEN 'aaaaaaaa-0006-0000-0000-000000000006' THEN 'Stretch denim jeans made of 98% cotton and 2% elastane with a slim fit and stone-washed finish.'
        WHEN 'aaaaaaaa-0007-0000-0000-000000000007' THEN 'Padded winter jacket with 90% goose down filling, waterproof shell, and removable hood.'
        WHEN 'aaaaaaaa-0008-0000-0000-000000000008' THEN 'LED desk lamp with adjustable 2700-6500K color temperature, touch dimmer, and USB port.'
        WHEN 'aaaaaaaa-0009-0000-0000-000000000009' THEN 'Set of 2 hypoallergenic microfiber cushions with 400-thread-count Egyptian cotton covers, 50x70cm.'
        WHEN 'aaaaaaaa-000a-0000-0000-00000000000a' THEN 'Hand-woven wool rug with geometric pattern, 160x230cm.'
        ELSE description
    END,
    category = CASE category
        WHEN 'Elettronica' THEN 'Electronics'
        WHEN 'Abbigliamento' THEN 'Clothing'
        WHEN 'Casa' THEN 'Home'
        ELSE category
    END
WHERE id IN (
    'aaaaaaaa-0001-0000-0000-000000000001',
    'aaaaaaaa-0002-0000-0000-000000000002',
    'aaaaaaaa-0003-0000-0000-000000000003',
    'aaaaaaaa-0004-0000-0000-000000000004',
    'aaaaaaaa-0005-0000-0000-000000000005',
    'aaaaaaaa-0006-0000-0000-000000000006',
    'aaaaaaaa-0007-0000-0000-000000000007',
    'aaaaaaaa-0008-0000-0000-000000000008',
    'aaaaaaaa-0009-0000-0000-000000000009',
    'aaaaaaaa-000a-0000-0000-00000000000a'
);
