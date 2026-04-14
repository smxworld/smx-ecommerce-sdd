const CATEGORY_PLACEHOLDERS = {
  Electronics: 'https://placehold.co/400x300/0f172a/f8fafc?text=Electronics',
  Clothing: 'https://placehold.co/400x300/7c2d12/fff7ed?text=Clothing',
  Home: 'https://placehold.co/400x300/14532d/f0fdf4?text=Home',
}

export function getProductImageUrl(product) {
  return CATEGORY_PLACEHOLDERS[product?.category] ?? 'https://placehold.co/400x300/e2e8f0/1e293b?text=Product'
}

export function getCartItemImageUrl(item) {
  const label = encodeURIComponent(item?.productName ?? 'Cart+item')
  return `https://placehold.co/80x60/e2e8f0/1e293b?text=${label}`
}
