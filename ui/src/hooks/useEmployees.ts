import { useCallback, useEffect, useState } from 'react'
import { createEmployee, fetchEmployees } from '../codex-example/api/employees'
import type { Employee, EmployeePayload } from '../codex-example/api/types'

interface UseEmployeesResult {
  employees: Employee[]
  loading: boolean
  error: string | null
  reload: () => Promise<void>
  addEmployee: (payload: EmployeePayload) => Promise<void>
}

export const useEmployees = (): UseEmployeesResult => {
  const [employees, setEmployees] = useState<Employee[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const [error, setError] = useState<string | null>(null)

  const loadEmployees = useCallback(async () => {
    setLoading(true)
    try {
      const data = await fetchEmployees()
      setEmployees(data)
      setError(null)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load employees')
    } finally {
      setLoading(false)
    }
  }, [])

  const addEmployee = useCallback(async (payload: EmployeePayload) => {
    try {
      const employee = await createEmployee(payload)
      setEmployees((prev) => [...prev, employee])
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create employee')
      throw err
    }
  }, [])

  useEffect(() => {
    loadEmployees()
  }, [loadEmployees])

  return {
    employees,
    loading,
    error,
    reload: loadEmployees,
    addEmployee
  }
}
