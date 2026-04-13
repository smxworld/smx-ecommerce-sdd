-- Seed: 10 prodotti distribuiti in 3 categorie — attivo solo con profilo dev
-- UUID fissi per riferibilità tra script di seed

INSERT INTO smx_catalog.products (id, name, description, price, category, average_rating, review_count, search_score, created_at, updated_at) VALUES
('aaaaaaaa-0001-0000-0000-000000000001', 'Smartphone XPro',    'Smartphone di ultima generazione con display AMOLED 6.7", fotocamera 108MP e batteria da 5000mAh.',   699.00,  'Elettronica',    4.5, 3, 0.9, NOW(), NOW()),
('aaaaaaaa-0002-0000-0000-000000000002', 'Laptop UltraSlim',   'Laptop ultraleggero 14" con processore octa-core, 16GB RAM, SSD 512GB e autonomia fino a 12 ore.',  1299.00, 'Elettronica',    4.2, 2, 0.8, NOW(), NOW()),
('aaaaaaaa-0003-0000-0000-000000000003', 'Cuffie Wireless',    'Cuffie over-ear con cancellazione attiva del rumore, Bluetooth 5.3 e autonomia di 30 ore.',          149.00,  'Elettronica',    0.0, 0, 0.5, NOW(), NOW()),
('aaaaaaaa-0004-0000-0000-000000000004', 'Tastiera Meccanica', 'Tastiera meccanica TKL con switch blue, retroilluminazione RGB e cavo USB-C rimovibile.',             89.00,   'Elettronica',    0.0, 0, 0.4, NOW(), NOW()),
('aaaaaaaa-0005-0000-0000-000000000005', 'T-Shirt Premium',    'T-shirt in cotone biologico 200g/m², taglio slim fit disponibile in 8 colori.',                        29.00,   'Abbigliamento',  0.0, 0, 0.3, NOW(), NOW()),
('aaaaaaaa-0006-0000-0000-000000000006', 'Jeans Slim Fit',     'Jeans in denim elasticizzato 98% cotone 2% elastan, vestibilità slim, lavaggio stone washed.',         59.00,   'Abbigliamento',  0.0, 0, 0.3, NOW(), NOW()),
('aaaaaaaa-0007-0000-0000-000000000007', 'Giacca Invernale',   'Giacca imbottita con riempimento in piuma d''oca 90%, impermeabile, con cappuccio removibile.',       199.00,  'Abbigliamento',  0.0, 0, 0.4, NOW(), NOW()),
('aaaaaaaa-0008-0000-0000-000000000008', 'Lampada da Tavolo',  'Lampada LED da tavolo con temperatura colore regolabile 2700-6500K, touch dimmer e porta USB.',         49.00,   'Casa',           0.0, 0, 0.2, NOW(), NOW()),
('aaaaaaaa-0009-0000-0000-000000000009', 'Set Cuscini',        'Set da 2 cuscini in microfibra anallergica con fodera in cotone egiziano 400 fili, 50x70cm.',          39.00,   'Casa',           0.0, 0, 0.2, NOW(), NOW()),
('aaaaaaaa-000a-0000-0000-00000000000a', 'Tappeto Moderno',    'Tappeto in lana intrecciata a mano con motivo geometrico, dimensioni 160x230cm.',                      129.00,  'Casa',           0.0, 0, 0.3, NOW(), NOW());
