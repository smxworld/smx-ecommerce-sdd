import { useParams, Link } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { getOrder } from '../api/orders'

const STATUS_LABELS = {
  PENDING: 'In attesa',
  CONFIRMED: 'Confermato',
  CANCELLED: 'Annullato',
  SHIPPED: 'Spedito',
  DELIVERED: 'Consegnato',
}

const STATUS_COLORS = {
  PENDING: 'bg-yellow-100 text-yellow-700',
  CONFIRMED: 'bg-green-100 text-green-700',
  CANCELLED: 'bg-red-100 text-red-700',
  SHIPPED: 'bg-blue-100 text-blue-700',
  DELIVERED: 'bg-indigo-100 text-indigo-700',
}

export default function OrderPage() {
  const { orderId } = useParams()

  const { data: order, isLoading, isError } = useQuery({
    queryKey: ['order', orderId],
    queryFn: () => getOrder(orderId),
    enabled: !!orderId,
  })

  if (isLoading) return <div className="max-w-2xl mx-auto px-4 py-8 text-gray-500">Caricamento...</div>
  if (isError || !order) return <div className="max-w-2xl mx-auto px-4 py-8 text-red-600">Ordine non trovato.</div>

  const total = (order.items ?? []).reduce((sum, i) => sum + i.unitPrice * i.quantity, 0)

  return (
    <div className="max-w-2xl mx-auto px-4 py-12 text-center">
      <div className="text-green-500 text-6xl mb-4">✓</div>
      <h1 className="text-2xl font-bold text-gray-900 mb-2">Ordine confermato!</h1>
      <p className="text-gray-500 mb-6">Grazie per il tuo acquisto.</p>

      <div className="bg-white border border-gray-200 rounded-lg p-6 text-left mb-8">
        <div className="flex items-center justify-between mb-4">
          <div>
            <p className="text-xs text-gray-500 uppercase tracking-wide">Numero ordine</p>
            <p className="font-mono text-sm text-gray-800">{order.orderId}</p>
          </div>
          <span
            className={`text-sm font-medium px-3 py-1 rounded-full ${
              STATUS_COLORS[order.status] ?? 'bg-gray-100 text-gray-700'
            }`}
          >
            {STATUS_LABELS[order.status] ?? order.status}
          </span>
        </div>

        <div className="space-y-2 mb-4">
          {(order.items ?? []).map((item) => (
            <div key={item.productId} className="flex justify-between text-sm">
              <span className="text-gray-700">{item.productName} × {item.quantity}</span>
              <span className="font-medium">€{(item.unitPrice * item.quantity).toFixed(2)}</span>
            </div>
          ))}
        </div>

        <div className="border-t border-gray-200 pt-3 flex justify-between font-bold">
          <span>Totale</span>
          <span>€{total.toFixed(2)}</span>
        </div>
      </div>

      <Link
        to="/"
        className="bg-indigo-600 text-white px-8 py-3 rounded-lg hover:bg-indigo-700 font-medium"
      >
        Continua a fare acquisti
      </Link>
    </div>
  )
}
