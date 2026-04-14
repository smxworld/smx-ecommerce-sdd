import { useState } from 'react'
import { useProductSearch } from '../hooks/useProducts'
import ProductCard from '../components/ProductCard'

const CATEGORIES = ['', 'Elettronica', 'Abbigliamento', 'Casa']

export default function HomePage() {
  const [query, setQuery] = useState('')
  const [category, setCategory] = useState('')
  const [minPrice, setMinPrice] = useState('')
  const [maxPrice, setMaxPrice] = useState('')
  const [page, setPage] = useState(0)
  const [searchParams, setSearchParams] = useState({
    q: '',
    category: undefined,
    minPrice: undefined,
    maxPrice: undefined,
  })

  const params = {
    ...searchParams,
    page,
    size: 9,
  }

  const { data, isLoading, isError } = useProductSearch(params)

  const products = data?.items ?? []
  const totalPages = data?.totalPages ?? 0

  const handleSearch = (e) => {
    e.preventDefault()
    setPage(0)
    setSearchParams({
      q: query,
      category: category || undefined,
      minPrice: minPrice || undefined,
      maxPrice: maxPrice || undefined,
    })
  }

  return (
    <div className="max-w-6xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-8 text-center">Scopri i nostri prodotti</h1>

      <form onSubmit={handleSearch} className="mb-8 flex flex-col sm:flex-row gap-3">
        <input
          type="text"
          placeholder="Cerca prodotti..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="flex-1 border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        />
        <select
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          className="border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          {CATEGORIES.map((c) => (
            <option key={c} value={c}>{c || 'Tutte le categorie'}</option>
          ))}
        </select>
        <input
          type="number"
          placeholder="Prezzo min"
          value={minPrice}
          onChange={(e) => setMinPrice(e.target.value)}
          className="w-28 border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        />
        <input
          type="number"
          placeholder="Prezzo max"
          value={maxPrice}
          onChange={(e) => setMaxPrice(e.target.value)}
          className="w-28 border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        />
        <button
          type="submit"
          className="bg-indigo-600 text-white px-6 py-2 rounded-lg hover:bg-indigo-700 font-medium"
        >
          Cerca
        </button>
      </form>

      {isLoading && <p className="text-center text-gray-500">Caricamento...</p>}
      {isError && <p className="text-center text-red-600">Errore nel caricamento dei prodotti.</p>}

      {!isLoading && !isError && products.length === 0 && (
        <p className="text-center text-gray-500">Nessun prodotto trovato.</p>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {products.map((p) => (
          <ProductCard key={p.id} product={p} />
        ))}
      </div>

      {totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-10">
          <button
            onClick={() => setPage((p) => Math.max(0, p - 1))}
            disabled={page === 0}
            className="px-4 py-2 border rounded-lg disabled:opacity-40 hover:bg-gray-50"
          >
            Precedente
          </button>
          <span className="px-4 py-2 text-gray-600">{page + 1} / {totalPages}</span>
          <button
            onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
            disabled={page === totalPages - 1}
            className="px-4 py-2 border rounded-lg disabled:opacity-40 hover:bg-gray-50"
          >
            Successivo
          </button>
        </div>
      )}
    </div>
  )
}
