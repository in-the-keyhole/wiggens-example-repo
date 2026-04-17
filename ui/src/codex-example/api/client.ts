import axios from 'axios'

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/codex-example/api/v1'
})

export type Employee = {
  id?: number
  firstName: string
  lastName: string
  email: string
}

export type TimesheetEntry = {
  id?: number
  dayOfWeek: number
  hours: number
  project?: string
  notes?: string
}

export type Timesheet = {
  id?: number
  employeeId: number
  weekStart: string
  entries: TimesheetEntry[]
}

export const EmployeesApi = {
  list: async () => (await api.get<Employee[]>('/employees')).data,
  create: async (e: Employee) => (await api.post<Employee>('/employees', e)).data
}

export const TimesheetsApi = {
  list: async () => (await api.get<Timesheet[]>('/timesheets')).data,
  create: async (t: Timesheet) => (await api.post<Timesheet>('/timesheets', t)).data
}

export const ReportsApi = {
  summary: async () => (await api.get<{ weekStart: string; totalHoursThisWeek: number; totalHoursAllTime: number }>('/reports/summary')).data
}

