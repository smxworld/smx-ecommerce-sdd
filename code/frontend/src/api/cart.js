import api from './axios'

export const getCart = () =>
  api.get('/cart').then((r) => r.data)

export const addToCart = (productId, quantity) =>
  api.post('/cart/items', { productId, quantity }).then((r) => r.data)

export const updateCartItem = (productId, quantity) =>
  api.put(`/cart/items/${productId}`, { quantity }).then((r) => r.data)

export const removeCartItem = (productId) =>
  api.delete(`/cart/items/${productId}`).then((r) => r.data)
