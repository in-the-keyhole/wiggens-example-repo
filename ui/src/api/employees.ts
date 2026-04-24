import api, { EmployeeDTO } from './client'

export async function listEmployees(): Promise<EmployeeDTO[]> {
  const resp = await api.get<EmployeeDTO[] | { content: EmployeeDTO[] }>('/employees')
  return Array.isArray(resp.data)
    ? resp.data
    : (resp.data && Array.isArray((resp.data as any).content))
    ? (resp.data as any).content
    : []
}

export async function createEmployee(input: Omit<EmployeeDTO, 'id'>): Promise<EmployeeDTO> {
  const resp = await api.post<EmployeeDTO>('/employees', input)
  return resp.data
}

export async function updateEmployee(id: number, input: Omit<EmployeeDTO, 'id'>): Promise<EmployeeDTO> {
  const resp = await api.put<EmployeeDTO>(`/employees/${id}`, input)
  return resp.data
}

export async function deleteEmployee(id: number): Promise<void> {
  await api.delete<void>(`/employees/${id}`)
}

export default {
  listEmployees,
  createEmployee,
  updateEmployee,
  deleteEmployee,
}

