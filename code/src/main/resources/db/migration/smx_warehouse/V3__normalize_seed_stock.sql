UPDATE smx_warehouse.stock
SET quantity_reserved = 0,
    updated_at = NOW()
WHERE product_id IN (
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
