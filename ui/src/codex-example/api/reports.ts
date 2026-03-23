import { apiClient } from './client'
import type { EmployeeHoursReport } from './types'

export const fetchEmployeeHoursReport = async (
  employeeId: string,
  startDate?: string,
  endDate?: string
): Promise<EmployeeHoursReport> => {
  const { data } = await apiClient.get<EmployeeHoursReport>('/reports/employee-hours', {
    params: {
      employeeId,
      start: startDate,
      end: endDate
    }
  })
  return data
}
