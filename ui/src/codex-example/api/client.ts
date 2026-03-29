import axios from 'axios'

export const api = axios.create({
  baseURL: 'http://localhost:8080/codex-example/api/v1'
})

export type Employee = { id: number; firstName: string; lastName: string }
export type Project = { id: number; name: string }
export type TimesheetEntry = { projectId: number; projectName?: string; dayOfWeek: string; hours: number }
export type Timesheet = { id: number; employeeId: number; weekStart: string; totalHours: number; entries: TimesheetEntry[] }

export async function listEmployees() {
  const { data } = await api.get<Employee[]>('/employees')
  return data
}

export async function createEmployee(firstName: string, lastName: string) {
  const { data } = await api.post<Employee>('/employees', { firstName, lastName })
  return data
}

export async function listProjects() {
  const { data } = await api.get<Project[]>('/projects')
  return data
}

export async function createProject(name: string) {
  const { data } = await api.post<Project>('/projects', { name })
  return data
}

export async function getTimesheet(employeeId: number, weekStart: string) {
  const { data } = await api.get<Timesheet>('/timesheets', { params: { employeeId, weekStart } })
  return data
}

export async function upsertTimesheet(payload: { employeeId: number; weekStart: string; entries: { projectId: number; dayOfWeek: string; hours: number }[] }) {
  const { data } = await api.post<Timesheet>('/timesheets', payload)
  return data
}

export async function getWeeklyDashboard(weekStart: string) {
  const { data } = await api.get<{ employeeId: number; employeeName: string; totalHours: number }[]>('/dashboard/weekly', { params: { weekStart } })
  return data
}

