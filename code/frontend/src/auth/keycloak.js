import Keycloak from 'keycloak-js'

const keycloak = new Keycloak({
  url: 'http://localhost:8180',
  realm: 'smxworld',
  clientId: 'smxworld-frontend',
})

export default keycloak
