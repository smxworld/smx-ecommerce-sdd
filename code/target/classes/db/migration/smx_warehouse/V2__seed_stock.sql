-- Seed: stock iniziale per i 10 prodotti del catalogo
-- UUID fissi — product_id corrisponde ai prodotti in smx_catalog V2

INSERT INTO smx_warehouse.stock (id, product_id, quantity_total, quantity_reserved, version, updated_at) VALUES
('bbbbbbbb-0001-0000-0000-000000000001', 'aaaaaaaa-0001-0000-0000-000000000001',  50,  0, 0, NOW()),
('bbbbbbbb-0002-0000-0000-000000000002', 'aaaaaaaa-0002-0000-0000-000000000002',  20,  0, 0, NOW()),
('bbbbbbbb-0003-0000-0000-000000000003', 'aaaaaaaa-0003-0000-0000-000000000003', 100,  0, 0, NOW()),
('bbbbbbbb-0004-0000-0000-000000000004', 'aaaaaaaa-0004-0000-0000-000000000004',  75,  0, 0, NOW()),
('bbbbbbbb-0005-0000-0000-000000000005', 'aaaaaaaa-0005-0000-0000-000000000005', 200,  0, 0, NOW()),
('bbbbbbbb-0006-0000-0000-000000000006', 'aaaaaaaa-0006-0000-0000-000000000006', 150,  0, 0, NOW()),
('bbbbbbbb-0007-0000-0000-000000000007', 'aaaaaaaa-0007-0000-0000-000000000007',  40,  0, 0, NOW()),
('bbbbbbbb-0008-0000-0000-000000000008', 'aaaaaaaa-0008-0000-0000-000000000008',  80,  0, 0, NOW()),
('bbbbbbbb-0009-0000-0000-000000000009', 'aaaaaaaa-0009-0000-0000-000000000009', 120,  0, 0, NOW()),
('bbbbbbbb-000a-0000-0000-00000000000a', 'aaaaaaaa-000a-0000-0000-00000000000a',  30,  0, 0, NOW());
