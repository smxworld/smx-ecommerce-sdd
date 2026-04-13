import { useQuery } from '@tanstack/react-query'
import { searchProducts, getProduct, getReviews } from '../api/products'

export const useProductSearch = (params) =>
  useQuery({
    queryKey: ['products', params],
    queryFn: () => searchProducts(params),
    keepPreviousData: true,
  })

export const useProduct = (id) =>
  useQuery({
    queryKey: ['product', id],
    queryFn: () => getProduct(id),
    enabled: !!id,
  })

export const useReviews = (productId) =>
  useQuery({
    queryKey: ['reviews', productId],
    queryFn: () => getReviews(productId),
    enabled: !!productId,
  })
