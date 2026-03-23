import { apiClient } from './client'
import type { Timesheet, TimesheetPayload } from './types'

export const fetchTimesheetsForEmployee = async (employeeId: string): Promise<Timesheet[]> => {
  const { data } = await apiClient.get<Timesheet[]>('/timesheets', {
    params: { employeeId }
  })
  return data
}

export const createTimesheet = async (payload: TimesheetPayload): Promise<Timesheet> => {
  const { data } = await apiClient.post<Timesheet>('/timesheets', payload)
  return data
}

export const updateTimesheet = async (id: string, payload: TimesheetPayload): Promise<Timesheet> => {
  const { data } = await apiClient.put<Timesheet>(`/timesheets/${id}`, payload)
  return data
}
