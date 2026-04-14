import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useCart } from '../hooks/useCart'
import { checkout } from '../api/orders'

const EMPTY_ADDRESS = {
  firstName: '',
  lastName: '',
  street: '',
  city: '',
  postalCode: '',
  country: 'IT',
}

export default function CheckoutPage() {
  const navigate = useNavigate()
  const { data: cart } = useCart()
  const [address, setAddress] = useState(EMPTY_ADDRESS)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const items = cart?.items ?? []
  const total = items.reduce((sum, i) => sum + i.unitPrice * i.quantity, 0)

  const handleChange = (e) => {
    setAddress((prev) => ({ ...prev, [e.target.name]: e.target.value }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError(null)
    try {
      const order = await checkout(address)
      navigate(`/orders/${order.orderId}`)
    } catch (err) {
      setError(err.response?.data?.message ?? 'Checkout failed. Please try again.')
      setLoading(false)
    }
  }

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900 mb-8">Checkout</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
        <form onSubmit={handleSubmit} className="space-y-4">
          <h2 className="text-lg font-semibold text-gray-800 mb-2">Shipping address</h2>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">First name</label>
              <input
                name="firstName"
                required
                value={address.firstName}
                onChange={handleChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Last name</label>
              <input
                name="lastName"
                required
                value={address.lastName}
                onChange={handleChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Street</label>
            <input
              name="street"
              required
              value={address.street}
              onChange={handleChange}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">City</label>
              <input
                name="city"
                required
                value={address.city}
                onChange={handleChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">ZIP code</label>
              <input
                name="postalCode"
                required
                value={address.postalCode}
                onChange={handleChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Country</label>
            <input
              name="country"
              required
              value={address.country}
              onChange={handleChange}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          {error && <p className="text-red-600 text-sm">{error}</p>}

          <button
            type="submit"
            disabled={loading || items.length === 0}
            className="w-full bg-indigo-600 text-white px-6 py-3 rounded-lg hover:bg-indigo-700 font-medium disabled:opacity-50 mt-2"
          >
            {loading ? 'Confirming...' : 'Confirm order'}
          </button>
        </form>

        <div>
          <h2 className="text-lg font-semibold text-gray-800 mb-4">Order summary</h2>
          <div className="bg-gray-50 rounded-lg border border-gray-200 p-4 space-y-3">
            {items.map((item) => (
              <div key={item.productId} className="flex justify-between text-sm">
                <span className="text-gray-700">{item.productName} × {item.quantity}</span>
                <span className="font-medium">€{(item.unitPrice * item.quantity).toFixed(2)}</span>
              </div>
            ))}
            <div className="border-t border-gray-300 pt-3 flex justify-between font-bold">
              <span>Total</span>
              <span>€{total.toFixed(2)}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
