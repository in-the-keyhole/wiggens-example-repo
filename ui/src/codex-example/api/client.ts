import axios from 'axios'

export const api = axios.create({
  baseURL: '/codex-example/api/v1'
})

export type Employee = { id: number; name: string; email: string }

export async function createEmployee(payload: Omit<Employee, 'id'>) {
  const { data } = await api.post<Employee>('/employees', payload)
  return data
}

export async function listEmployees() {
  const { data } = await api.get<Employee[]>('/employees')
  return data
}

export type EntryDto = { dayOfWeek: string; hours: number; project: string }
export type TimesheetResponse = { id: number; employeeId: number; weekStart: string; entries: EntryDto[]; totalHours: number }

export async function createTimesheet(payload: { employeeId: number; weekStart: string; entries: EntryDto[] }) {
  const { data } = await api.post<TimesheetResponse>('/timesheets', payload)
  return data
}

export async function getSummary(start: string, end: string) {
  const { data } = await api.get<{ employeeId: number; employeeName: string; weekStart: string; totalHours: number }[]>(
    '/reports/summary',
    { params: { start, end } }
  )
  return data
}

