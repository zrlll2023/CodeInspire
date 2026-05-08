import api from './index'

export const planApi = {
  getPlans(status?: string) {
    return api.get('/plans', { params: { status } })
  },

  createPlan(data: any) {
    return api.post('/plans', data)
  },

  updatePlan(id: number, data: any) {
    return api.put(`/plans/${id}`, data)
  },

  deletePlan(id: number) {
    return api.delete(`/plans/${id}`)
  },

  completePlan(id: number) {
    return api.post(`/plans/${id}/complete`)
  },

  addTask(planId: number, data: any) {
    return api.post(`/plans/${planId}/tasks`, data)
  },

  getTasks(planId: number) {
    return api.get(`/plans/${planId}/tasks`)
  }
}

export const taskApi = {
  getAllTasks(status?: string) {
    return api.get('/tasks', { params: { status } })
  },

  createTask(data: any) {
    return api.post('/tasks', data)
  },

  updateTask(id: number, data: any) {
    return api.put(`/tasks/${id}`, data)
  },

  deleteTask(id: number) {
    return api.delete(`/tasks/${id}`)
  },

  completeTask(id: number) {
    return api.post(`/tasks/${id}/complete`)
  },

  getTodayTasks() {
    return api.get('/tasks/today')
  },

  getOverdueTasks() {
    return api.get('/tasks/overdue')
  }
}
