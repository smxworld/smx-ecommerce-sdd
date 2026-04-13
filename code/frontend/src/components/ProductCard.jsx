import { Link } from 'react-router-dom'
import StarRating from './StarRating'

export default function ProductCard({ product }) {
  const inStock = product.stockAvailable > 0

  return (
    <Link
      to={`/products/${product.id}`}
      className="bg-white rounded-lg shadow hover:shadow-md transition-shadow overflow-hidden flex flex-col"
    >
      <img
        src="https://placehold.co/400x300"
        alt={product.name}
        className="w-full h-48 object-cover"
      />
      <div className="p-4 flex flex-col flex-1">
        <p className="text-xs text-gray-500 uppercase tracking-wide mb-1">{product.category}</p>
        <h3 className="font-semibold text-gray-900 mb-2 flex-1">{product.name}</h3>
        <div className="flex items-center gap-1 mb-2">
          <StarRating rating={product.averageRating} />
          <span className="text-xs text-gray-500">({product.reviewCount})</span>
        </div>
        <div className="flex items-center justify-between mt-auto">
          <span className="text-lg font-bold text-gray-900">
            €{Number(product.price).toFixed(2)}
          </span>
          <span
            className={`text-xs font-medium px-2 py-1 rounded-full ${
              inStock
                ? 'bg-green-100 text-green-700'
                : 'bg-red-100 text-red-700'
            }`}
          >
            {inStock ? 'Disponibile' : 'Esaurito'}
          </span>
        </div>
      </div>
    </Link>
  )
}
