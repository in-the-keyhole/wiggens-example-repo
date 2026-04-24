import { useCallback, useEffect, useState } from 'react'
import type { EmployeeDTO } from '../api/client'
import employeesApi from '../api/employees'

export function useEmployees() {
  const [employees, setEmployees] = useState<EmployeeDTO[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const list = await employeesApi.listEmployees()
      setEmployees(list)
    } catch (e: any) {
      setError(e?.message ?? 'Failed to load employees')
    } finally {
      setLoading(false)
    }
  }, [])

  const add = useCallback(async (data: Omit<EmployeeDTO, 'id'>) => {
    const created = await employeesApi.createEmployee(data)
    setEmployees(prev => [...prev, created])
    return created
  }, [])

  const update = useCallback(async (id: number, data: Omit<EmployeeDTO, 'id'>) => {
    const updated = await employeesApi.updateEmployee(id, data)
    setEmployees(prev => prev.map(e => e.id === id ? updated : e))
    return updated
  }, [])

  const remove = useCallback(async (id: number) => {
    await employeesApi.deleteEmployee(id)
    setEmployees(prev => prev.filter(e => e.id !== id))
  }, [])

  useEffect(() => { load() }, [load])

  return { employees, loading, error, reload: load, add, update, remove }
}

export default useEmployees

