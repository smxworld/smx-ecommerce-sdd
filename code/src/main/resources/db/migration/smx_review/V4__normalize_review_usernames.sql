UPDATE smx_review.reviews
SET user_id = CASE id
    WHEN 'cccccccc-0001-0000-0000-000000000001' THEN 'buyer@smxworld.local'
    WHEN 'cccccccc-0002-0000-0000-000000000002' THEN 'user2@smxworld.local'
    WHEN 'cccccccc-0003-0000-0000-000000000003' THEN 'user3@smxworld.local'
    WHEN 'cccccccc-0004-0000-0000-000000000004' THEN 'buyer@smxworld.local'
    WHEN 'cccccccc-0005-0000-0000-000000000005' THEN 'user4@smxworld.local'
    ELSE user_id
END
WHERE id IN (
    'cccccccc-0001-0000-0000-000000000001',
    'cccccccc-0002-0000-0000-000000000002',
    'cccccccc-0003-0000-0000-000000000003',
    'cccccccc-0004-0000-0000-000000000004',
    'cccccccc-0005-0000-0000-000000000005'
);
