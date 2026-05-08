import api from './index'

export const profileApi = {
  getProfile() {
    return api.get('/profile')
  },

  getFullProfile() {
    return api.get('/profile/full')
  },

  initProfile(data: any) {
    return api.post('/profile/init', data)
  },

  updateProfile(data: any) {
    return api.put('/profile', data)
  },

  updateFullProfile(data: any) {
    return api.put('/profile/full', data)
  },

  getCompleteness() {
    return api.get('/profile/completeness')
  },

  getSuggestions() {
    return api.get('/profile/suggestions')
  }
}
