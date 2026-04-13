import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getCart, addToCart, updateCartItem, removeCartItem } from '../api/cart'

export const useCart = () =>
  useQuery({
    queryKey: ['cart'],
    queryFn: getCart,
  })

export const useAddToCart = () => {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: ({ productId, quantity }) => addToCart(productId, quantity),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['cart'] }),
  })
}

export const useUpdateCartItem = () => {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: ({ productId, quantity }) => updateCartItem(productId, quantity),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['cart'] }),
  })
}

export const useRemoveCartItem = () => {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (productId) => removeCartItem(productId),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['cart'] }),
  })
}
