import axios from 'axios'

export const api = axios.create({
  baseURL: '/codex-example/api/v1',
})

export type Stats = {
  employeeCount: number
  totalHoursThisWeek: string
  totalHoursAllTime: string
}

export async function fetchStats(): Promise<Stats> {
  const res = await api.get('/timesheets/stats')
  return res.data
}

export type Employee = {
  id?: number
  firstName: string
  lastName: string
  email: string
}

export async function listEmployees(): Promise<Employee[]> {
  const res = await api.get('/employees')
  // Ensure we always return an array to avoid UI runtime errors
  return Array.isArray(res.data) ? res.data : []
}

export async function createEmployee(e: Employee): Promise<Employee> {
  const res = await api.post('/employees', e)
  return res.data
}

export async function updateEmployee(id: number, e: Employee): Promise<Employee> {
  const res = await api.put(`/employees/${id}` , e)
  return res.data
}

export async function deleteEmployee(id: number): Promise<void> {
  await api.delete(`/employees/${id}`)
}

export type TimesheetEntry = { id?: number; date: string; hours: number }
export type Timesheet = { id?: number; employeeId: number; weekStart: string; entries: TimesheetEntry[] }

export async function saveTimesheet(ts: Timesheet): Promise<Timesheet> {
  const res = await api.post('/timesheets', ts)
  return res.data
}

export async function listTimesheetsByEmployee(employeeId: number): Promise<Timesheet[]> {
  const res = await api.get(`/timesheets/employee/${employeeId}`)
  return res.data
}
