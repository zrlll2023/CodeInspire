import api from './index'

export const chatApi = {
  sendMessage(data: { message: string; scene?: string; sessionId?: string }) {
    return api.post('/chat/send', data)
  },

  getHistory(sessionId: string) {
    return api.get('/chat/history', { params: { sessionId } })
  }
}
