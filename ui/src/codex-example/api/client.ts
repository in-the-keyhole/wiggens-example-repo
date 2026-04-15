import axios from 'axios'

export const api = axios.create({
  baseURL: 'http://localhost:8080/codex-example/api/v1'
})

export type Employee = { id: number; name: string; email: string }
export type Timesheet = {
  id?: number
  employeeId: number
  weekStart: string // yyyy-MM-dd
  mon: number; tue: number; wed: number; thu: number; fri: number; sat: number; sun: number
  total?: number
}

export async function listEmployees() {
  const { data } = await api.get<Employee[]>('/employees')
  return data
}

export async function createEmployee(payload: Omit<Employee, 'id'>) {
  const { data } = await api.post<Employee>('/employees', payload)
  return data
}

export async function submitTimesheet(payload: Timesheet) {
  const { data } = await api.post<Timesheet>('/timesheets', payload)
  return data
}

export async function getTimesheet(employeeId: number, weekStart: string) {
  const { data } = await api.get<Timesheet>('/timesheets', { params: { employeeId, weekStart } })
  return data
}

export async function getSummary(from: string, to: string) {
  const { data } = await api.get<{ employeeId: number; employeeName: string; totalHours: number }[]>('/reports/summary', { params: { from, to } })
  return data
}

