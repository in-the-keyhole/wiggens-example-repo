import { useCallback, useEffect, useState } from 'react'
import { createTimesheet, fetchTimesheetsForEmployee, updateTimesheet } from '../codex-example/api/timesheets'
import type { Timesheet, TimesheetPayload } from '../codex-example/api/types'

interface UseTimesheetsResult {
  timesheets: Timesheet[]
  loading: boolean
  error: string | null
  refresh: () => Promise<void>
  create: (payload: TimesheetPayload) => Promise<Timesheet>
  update: (id: string, payload: TimesheetPayload) => Promise<Timesheet>
}

export const useTimesheets = (employeeId?: string): UseTimesheetsResult => {
  const [timesheets, setTimesheets] = useState<Timesheet[]>([])
  const [loading, setLoading] = useState<boolean>(false)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    if (!employeeId) {
      setTimesheets([])
      return
    }
    setLoading(true)
    try {
      const data = await fetchTimesheetsForEmployee(employeeId)
      setTimesheets(data)
      setError(null)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load timesheets')
    } finally {
      setLoading(false)
    }
  }, [employeeId])

  const create = useCallback(async (payload: TimesheetPayload) => {
    try {
      const created = await createTimesheet(payload)
      await load()
      return created
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create timesheet')
      throw err
    }
  }, [load])

  const update = useCallback(async (id: string, payload: TimesheetPayload) => {
    try {
      const updated = await updateTimesheet(id, payload)
      await load()
      return updated
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update timesheet')
      throw err
    }
  }, [load])

  useEffect(() => {
    void load()
  }, [load])

  return {
    timesheets,
    loading,
    error,
    refresh: load,
    create,
    update
  }
}
