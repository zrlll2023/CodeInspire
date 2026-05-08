import api from './index'

export const authApi = {
  register(data: { username: string; email: string; password: string }) {
    return api.post('/auth/register', data)
  },

  login(data: { username: string; password: string }) {
    return api.post('/auth/login', data)
  },

  logout() {
    return api.post('/auth/logout')
  }
}
