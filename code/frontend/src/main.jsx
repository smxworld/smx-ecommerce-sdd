import React from 'react'
import ReactDOM from 'react-dom/client'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import App from './App'
import keycloak from './auth/keycloak'
import './index.css'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      staleTime: 30_000,
    },
  },
})

keycloak
  .init({
    onLoad: 'login-required',
    checkLoginIframe: false,
    pkceMethod: 'S256',
  })
  .then((authenticated) => {
    if (!authenticated) {
      keycloak.login()
      return
    }

    // Refresh token automatically 30 seconds before expiry
    setInterval(() => {
      keycloak.updateToken(30).catch(() => keycloak.logout())
    }, 10_000)

    ReactDOM.createRoot(document.getElementById('root')).render(
      <React.StrictMode>
        <QueryClientProvider client={queryClient}>
          <App />
        </QueryClientProvider>
      </React.StrictMode>
    )
  })
  .catch(() => {
    document.getElementById('root').innerHTML =
      '<p style="color:red;padding:2rem">Impossibile connettersi al server di autenticazione. Assicurati che Keycloak sia avviato su http://localhost:8180.</p>'
  })
