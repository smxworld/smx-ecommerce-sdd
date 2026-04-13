import { Link } from 'react-router-dom'
import keycloak from '../auth/keycloak'

export default function Navbar() {
  return (
    <nav className="bg-white shadow-sm border-b border-gray-200">
      <div className="max-w-6xl mx-auto px-4 py-3 flex items-center justify-between">
        <Link to="/" className="text-xl font-bold text-indigo-600">SMX Store</Link>
        <div className="flex items-center gap-4">
          <Link to="/cart" className="text-gray-600 hover:text-indigo-600 font-medium">
            Carrello
          </Link>
          <button
            onClick={() => keycloak.logout()}
            className="text-sm text-gray-500 hover:text-red-600"
          >
            Esci
          </button>
        </div>
      </div>
    </nav>
  )
}
