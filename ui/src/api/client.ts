import axios from 'axios'

const api = axios.create({
  baseURL: '/codex-example/api/v1'
})

export default api

export type EmployeeDTO = {
  id?: number
  name: string
  email: string
}

export type TimesheetDTO = {
  id?: number
  employeeId: number
  weekStart: string
  hoursMon: number
  hoursTue: number
  hoursWed: number
  hoursThu: number
  hoursFri: number
  hoursSat: number
  hoursSun: number
}

