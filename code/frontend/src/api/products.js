import api from './axios'

export const searchProducts = (params) =>
  api.get('/search', { params }).then((r) => r.data)

export const getProduct = (id) =>
  api.get(`/products/${id}`).then((r) => r.data)

export const getReviews = (productId) =>
  api.get(`/reviews/${productId}`).then((r) => r.data)
