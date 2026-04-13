import axios from 'axios'
import keycloak from '../auth/keycloak'

const api = axios.create({
  baseURL: '/api',
})

api.interceptors.request.use((config) => {
  if (keycloak.token) {
    config.headers.Authorization = `Bearer ${keycloak.token}`
  }
  return config
})

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      try {
        await keycloak.updateToken(30)
        error.config.headers.Authorization = `Bearer ${keycloak.token}`
        return api.request(error.config)
      } catch {
        keycloak.login()
      }
    }
    return Promise.reject(error)
  }
)

export default api
