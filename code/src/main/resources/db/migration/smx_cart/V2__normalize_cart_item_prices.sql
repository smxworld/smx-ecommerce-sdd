UPDATE smx_cart.cart_items ci
SET unit_price = p.price,
    product_name = p.name
FROM smx_catalog.products p
WHERE ci.product_id = p.id
  AND (
      ci.unit_price = 1.0000
      OR ci.product_name LIKE 'Product-%'
  );
