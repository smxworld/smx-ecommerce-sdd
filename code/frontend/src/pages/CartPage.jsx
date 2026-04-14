import { Link, useNavigate } from 'react-router-dom'
import { useCart, useUpdateCartItem, useRemoveCartItem } from '../hooks/useCart'
import { getCartItemImageUrl } from '../utils/productImages'

export default function CartPage() {
  const { data: cart, isLoading } = useCart()
  const updateItem = useUpdateCartItem()
  const removeItem = useRemoveCartItem()
  const navigate = useNavigate()

  if (isLoading) return <div className="max-w-3xl mx-auto px-4 py-8 text-gray-500">Loading...</div>

  const items = cart?.items ?? []
  const total = items.reduce((sum, i) => sum + i.unitPrice * i.quantity, 0)

  if (items.length === 0) {
    return (
      <div className="max-w-3xl mx-auto px-4 py-16 text-center">
        <p className="text-gray-500 text-lg mb-6">Your cart is empty.</p>
        <Link to="/" className="bg-indigo-600 text-white px-6 py-3 rounded-lg hover:bg-indigo-700 font-medium">
          Continue shopping
        </Link>
      </div>
    )
  }

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900 mb-8">Cart</h1>

      <div className="space-y-4 mb-8">
        {items.map((item) => (
          <div key={item.productId} className="bg-white border border-gray-200 rounded-lg p-4 flex items-center gap-4">
            <img src={getCartItemImageUrl(item)} alt={item.productName} className="w-20 h-16 object-cover rounded" />
            <div className="flex-1">
              <p className="font-semibold text-gray-900">{item.productName}</p>
              <p className="text-gray-500 text-sm">€{Number(item.unitPrice).toFixed(2)} each</p>
            </div>
            <div className="flex items-center gap-2">
              <input
                type="number"
                min={1}
                value={item.quantity}
                onChange={(e) =>
                  updateItem.mutate({ productId: item.productId, quantity: Math.max(1, Number(e.target.value)) })
                }
                className="w-16 border border-gray-300 rounded px-2 py-1 text-center"
              />
            </div>
            <p className="font-semibold text-gray-900 w-24 text-right">
              €{(item.unitPrice * item.quantity).toFixed(2)}
            </p>
            <button
              onClick={() => removeItem.mutate(item.productId)}
              className="text-red-500 hover:text-red-700 text-sm"
            >
              Remove
            </button>
          </div>
        ))}
      </div>

      <div className="flex items-center justify-between">
        <span className="text-xl font-bold text-gray-900">Total: €{total.toFixed(2)}</span>
        <button
          onClick={() => navigate('/checkout')}
          className="bg-indigo-600 text-white px-8 py-3 rounded-lg hover:bg-indigo-700 font-medium"
        >
          Proceed to checkout
        </button>
      </div>
    </div>
  )
}
