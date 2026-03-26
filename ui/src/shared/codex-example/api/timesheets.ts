import { api } from './client'

export type TimesheetEntry = { projectCode: string; task: string; hours: number }
export type Timesheet = { id?: number; employeeId: string; weekStart: string; entries: TimesheetEntry[] }

export async function createTimesheet(t: Timesheet): Promise<Timesheet> {
  const res = await api.post('/timesheets', t)
  return res.data
}

export async function getTimesheetsForWeek(employeeId: string, weekStart: string): Promise<Timesheet[]> {
  const res = await api.get('/timesheets', { params: { employeeId, weekStart } })
  return res.data
}

export async function getDashboard(weekStart: string): Promise<{ weekStart: string; totalHours: number }> {
  const res = await api.get('/dashboard', { params: { weekStart } })
  return res.data
}

