import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useProduct, useReviews } from '../hooks/useProducts'
import { useAddToCart } from '../hooks/useCart'
import StarRating from '../components/StarRating'

export default function ProductPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [quantity, setQuantity] = useState(1)

  const { data: product, isLoading, isError } = useProduct(id)
  const { data: reviews = [] } = useReviews(id)
  const addToCart = useAddToCart()

  if (isLoading) return <div className="max-w-4xl mx-auto px-4 py-8 text-gray-500">Loading...</div>
  if (isError || !product) return <div className="max-w-4xl mx-auto px-4 py-8 text-red-600">Product not found.</div>

  const inStock = product.stockAvailable > 0

  const handleAddToCart = async () => {
    await addToCart.mutateAsync({ productId: product.id, quantity })
    navigate('/cart')
  }

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
        <div>
          <img
            src="https://placehold.co/400x300"
            alt={product.name}
            className="w-full rounded-lg shadow"
          />
        </div>
        <div className="flex flex-col">
          <p className="text-xs text-gray-500 uppercase tracking-wide mb-1">{product.category}</p>
          <h1 className="text-2xl font-bold text-gray-900 mb-3">{product.name}</h1>
          <div className="flex items-center gap-2 mb-4">
            <StarRating rating={product.averageRating} />
            <span className="text-sm text-gray-500">({product.reviewCount} reviews)</span>
          </div>
          <p className="text-3xl font-bold text-indigo-600 mb-4">€{Number(product.price).toFixed(2)}</p>
          <span
            className={`self-start text-xs font-medium px-3 py-1 rounded-full mb-6 ${
              inStock ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
            }`}
          >
            {inStock ? `In stock (${product.stockAvailable} pcs)` : 'Out of stock'}
          </span>

          {product.description && (
            <p className="text-gray-700 mb-6">{product.description}</p>
          )}

          {inStock && (
            <div className="flex items-center gap-3 mt-auto">
              <input
                type="number"
                min={1}
                max={product.stockAvailable}
                value={quantity}
                onChange={(e) => setQuantity(Math.max(1, Number(e.target.value)))}
                className="w-20 border border-gray-300 rounded-lg px-3 py-2 text-center"
              />
              <button
                onClick={handleAddToCart}
                disabled={addToCart.isPending}
                className="flex-1 bg-indigo-600 text-white px-6 py-3 rounded-lg hover:bg-indigo-700 font-medium disabled:opacity-50"
              >
                {addToCart.isPending ? 'Adding...' : 'Add to cart'}
              </button>
            </div>
          )}
        </div>
      </div>

      {reviews.length > 0 && (
        <div className="mt-12">
          <h2 className="text-xl font-bold text-gray-900 mb-6">Reviews</h2>
          <div className="space-y-4">
            {reviews.map((r) => (
              <div key={r.id} className="bg-white rounded-lg border border-gray-200 p-4">
                <div className="flex items-center gap-2 mb-2">
                  <StarRating rating={r.rating} />
                  <span className="text-sm text-gray-500">{r.userId}</span>
                </div>
                {r.text && <p className="text-gray-700">{r.text}</p>}
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
