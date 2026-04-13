-- Seed: 5 recensioni pre-caricate su Smartphone XPro e Laptop UltraSlim
-- UUID fissi — product_id corrisponde ai prodotti del catalog seed

-- 3 recensioni su Smartphone XPro (aaaaaaaa-0001-...)
INSERT INTO smx_review.reviews (id, product_id, user_id, order_id, rating, text, created_at) VALUES
('cccccccc-0001-0000-0000-000000000001',
 'aaaaaaaa-0001-0000-0000-000000000001',
 'acquirente@smx.local',
 'dddddddd-0001-0000-0000-000000000001',
 5,
 'Ottimo smartphone, fotocamera eccezionale e batteria duratura. Lo consiglio vivamente!',
 NOW() - INTERVAL '10 days'),

('cccccccc-0002-0000-0000-000000000002',
 'aaaaaaaa-0001-0000-0000-000000000001',
 'utente2@smx.local',
 'dddddddd-0002-0000-0000-000000000002',
 4,
 'Buon prodotto nel complesso. Il display è spettacolare. Manca solo il caricatore in confezione.',
 NOW() - INTERVAL '7 days'),

('cccccccc-0003-0000-0000-000000000003',
 'aaaaaaaa-0001-0000-0000-000000000001',
 'utente3@smx.local',
 'dddddddd-0003-0000-0000-000000000003',
 4,
 'Veloce e reattivo, l''interfaccia è pulita. Qualche app fa crashare di tanto in tanto.',
 NOW() - INTERVAL '3 days'),

-- 2 recensioni su Laptop UltraSlim (aaaaaaaa-0002-...)
('cccccccc-0004-0000-0000-000000000004',
 'aaaaaaaa-0002-0000-0000-000000000002',
 'acquirente@smx.local',
 'dddddddd-0004-0000-0000-000000000004',
 5,
 'Leggerissimo e potente. Lo uso tutto il giorno per lavoro senza problemi di autonomia.',
 NOW() - INTERVAL '15 days'),

('cccccccc-0005-0000-0000-000000000005',
 'aaaaaaaa-0002-0000-0000-000000000002',
 'utente4@smx.local',
 'dddddddd-0005-0000-0000-000000000005',
 3,
 'Buona qualità costruttiva ma si scalda un po'' sotto carico. SSD velocissimo.',
 NOW() - INTERVAL '5 days');
