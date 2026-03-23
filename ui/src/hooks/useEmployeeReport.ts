import { useCallback, useEffect, useState } from 'react'
import { fetchEmployeeHoursReport } from '../codex-example/api/reports'
import type { EmployeeHoursReport } from '../codex-example/api/types'

interface UseEmployeeReportParams {
  employeeId?: string
  startDate?: string
  endDate?: string
}

interface UseEmployeeReportResult {
  report: EmployeeHoursReport | null
  loading: boolean
  error: string | null
  refresh: () => Promise<void>
}

export const useEmployeeReport = ({
  employeeId,
  startDate,
  endDate
}: UseEmployeeReportParams): UseEmployeeReportResult => {
  const [report, setReport] = useState<EmployeeHoursReport | null>(null)
  const [loading, setLoading] = useState<boolean>(false)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    if (!employeeId) {
      setReport(null)
      return
    }
    setLoading(true)
    try {
      const data = await fetchEmployeeHoursReport(employeeId, startDate, endDate)
      setReport(data)
      setError(null)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load report')
    } finally {
      setLoading(false)
    }
  }, [employeeId, startDate, endDate])

  useEffect(() => {
    void load()
  }, [load])

  return {
    report,
    loading,
    error,
    refresh: load
  }
}
