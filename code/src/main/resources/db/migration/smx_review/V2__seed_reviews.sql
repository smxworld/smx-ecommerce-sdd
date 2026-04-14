-- Seed: 5 preloaded reviews for Smartphone XPro and Laptop UltraSlim
-- Fixed UUIDs — product_id matches the catalog seed products

-- 3 reviews for Smartphone XPro (aaaaaaaa-0001-...)
INSERT INTO smx_review.reviews (id, product_id, user_id, order_id, rating, text, created_at) VALUES
('cccccccc-0001-0000-0000-000000000001',
 'aaaaaaaa-0001-0000-0000-000000000001',
 'buyer@smxworld.local',
 'dddddddd-0001-0000-0000-000000000001',
 5,
 'Excellent smartphone, outstanding camera, and long-lasting battery. Highly recommended!',
 NOW() - INTERVAL '10 days'),

('cccccccc-0002-0000-0000-000000000002',
 'aaaaaaaa-0001-0000-0000-000000000001',
 'user2@smxworld.local',
 'dddddddd-0002-0000-0000-000000000002',
 4,
 'Good product overall. The display is stunning. It only misses a charger in the box.',
 NOW() - INTERVAL '7 days'),

('cccccccc-0003-0000-0000-000000000003',
 'aaaaaaaa-0001-0000-0000-000000000001',
 'user3@smxworld.local',
 'dddddddd-0003-0000-0000-000000000003',
 4,
 'Fast and responsive, and the interface feels clean. A few apps crash from time to time.',
 NOW() - INTERVAL '3 days'),

-- 2 reviews for Laptop UltraSlim (aaaaaaaa-0002-...)
('cccccccc-0004-0000-0000-000000000004',
 'aaaaaaaa-0002-0000-0000-000000000002',
 'buyer@smxworld.local',
 'dddddddd-0004-0000-0000-000000000004',
 5,
 'Very light and powerful. I use it all day for work without battery issues.',
 NOW() - INTERVAL '15 days'),

('cccccccc-0005-0000-0000-000000000005',
 'aaaaaaaa-0002-0000-0000-000000000002',
 'user4@smxworld.local',
 'dddddddd-0005-0000-0000-000000000005',
 3,
 'Good build quality, but it gets a little warm under load. The SSD is extremely fast.',
 NOW() - INTERVAL '5 days');
