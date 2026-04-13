import axios from 'axios'

export const api = axios.create({
  baseURL: '/codex-example/api/v1',
})

export type TimesheetItem = {
  workDate: string
  project: string
  hours: number
  note?: string
}

export type TimesheetRequest = {
  employeeId: number
  weekStart: string
  entries: TimesheetItem[]
}

export type TimesheetResponse = {
  id: number
  employeeId: number
  employeeName: string
  weekStart: string
  entries: TimesheetItem[]
}

export async function upsertTimesheet(payload: TimesheetRequest) {
  const { data } = await api.post<TimesheetResponse>('/timesheets', payload)
  return data
}

export async function getTimesheet(employeeId: number, weekStart: string) {
  const { data } = await api.get<TimesheetResponse>('/timesheets', { params: { employeeId, weekStart } })
  return data
}

export async function getSummary(from: string, to: string) {
  const { data } = await api.get<{ totalsByEmployee: Record<string, number> }>(`/reports/summary`, { params: { from, to } })
  return data
}

