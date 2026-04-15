import { api } from './client'

export type Employee = { id: number, name: string, email: string }

export async function listEmployees(): Promise<Employee[]> {
  const { data } = await api.get('/employees')
  return data
}

type UpsertReq = { employeeId: number, weekStart: string, mon: number, tue: number, wed: number, thu: number, fri: number, sat: number, sun: number, notes?: string }
export async function upsertTimesheet(req: UpsertReq) {
  const { data } = await api.post('/timesheets', req)
  return data
}

export async function summaryReport(from: string, to: string) {
  const { data } = await api.get(`/reports/summary`, { params: { from, to }})
  return data as { employeeId: number, employeeName: string, totalHours: number }[]
}

