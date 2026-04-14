UPDATE smx_review.reviews
SET text = CASE id
    WHEN 'cccccccc-0001-0000-0000-000000000001' THEN 'Excellent smartphone, outstanding camera, and long-lasting battery. Highly recommended!'
    WHEN 'cccccccc-0002-0000-0000-000000000002' THEN 'Good product overall. The display is stunning. It only misses a charger in the box.'
    WHEN 'cccccccc-0003-0000-0000-000000000003' THEN 'Fast and responsive, and the interface feels clean. A few apps crash from time to time.'
    WHEN 'cccccccc-0004-0000-0000-000000000004' THEN 'Very light and powerful. I use it all day for work without battery issues.'
    WHEN 'cccccccc-0005-0000-0000-000000000005' THEN 'Good build quality, but it gets a little warm under load. The SSD is extremely fast.'
    ELSE text
END
WHERE id IN (
    'cccccccc-0001-0000-0000-000000000001',
    'cccccccc-0002-0000-0000-000000000002',
    'cccccccc-0003-0000-0000-000000000003',
    'cccccccc-0004-0000-0000-000000000004',
    'cccccccc-0005-0000-0000-000000000005'
);
