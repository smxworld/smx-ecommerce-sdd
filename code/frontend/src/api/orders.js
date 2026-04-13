import api from './axios'

export const checkout = (shippingAddress) =>
  api.post('/checkout', { shippingAddress }).then((r) => r.data)

export const getOrder = (orderId) =>
  api.get(`/orders/${orderId}`).then((r) => r.data)
