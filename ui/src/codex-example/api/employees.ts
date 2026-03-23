import { apiClient } from './client'
import type { Employee, EmployeePayload } from './types'

export const fetchEmployees = async (): Promise<Employee[]> => {
  const { data } = await apiClient.get<Employee[]>('/employees')
  return data
}

export const createEmployee = async (payload: EmployeePayload): Promise<Employee> => {
  const { data } = await apiClient.post<Employee>('/employees', payload)
  return data
}
