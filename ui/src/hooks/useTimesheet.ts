import { useCallback, useState } from 'react'
import api, { TimesheetDTO } from '../api/client'

export function useTimesheet() {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const upsert = useCallback(async (dto: TimesheetDTO) => {
    setLoading(true); setError(null)
    try {
      const { data } = await api.post<TimesheetDTO>('/timesheets', dto)
      return data
    } catch (e: any) {
      setError(e?.message ?? 'Error saving timesheet')
      throw e
    } finally {
      setLoading(false)
    }
  }, [])

  return { upsert, loading, error }
}

