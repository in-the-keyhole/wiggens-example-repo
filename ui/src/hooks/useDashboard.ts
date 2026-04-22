import { useCallback, useEffect, useMemo, useState } from 'react'
import api, { EmployeeDTO, TimesheetDTO } from '../api/client'
import dayjs from 'dayjs'
import isoWeek from 'dayjs/plugin/isoWeek'

dayjs.extend(isoWeek)

export type DashboardStats = {
  employeeCount: number
  hoursThisWeek: number
  hoursToDate: number
  employees: EmployeeDTO[]
}

function sumHours(ts?: TimesheetDTO | null): number {
  if (!ts) return 0
  return ts.hoursMon + ts.hoursTue + ts.hoursWed + ts.hoursThu + ts.hoursFri + ts.hoursSat + ts.hoursSun
}

function sumHoursMany(list: TimesheetDTO[]): number {
  return list.reduce((acc, ts) => acc + sumHours(ts), 0)
}

export function useDashboard() {
  const [stats, setStats] = useState<DashboardStats>({ employeeCount: 0, hoursThisWeek: 0, hoursToDate: 0, employees: [] })
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const { data: employees } = await api.get<EmployeeDTO[]>('/employees')
      const employeeCount = employees.length
      const monday = dayjs().isoWeekday(1).format('YYYY-MM-DD')

      // For each employee, get this week's timesheet and all timesheets
      const weeklyPromises = employees.map(e => api
        .get<TimesheetDTO>(`/timesheets/employee/${e.id}/week`, { params: { start: monday } })
        .then(r => r.data)
        .catch(() => null as TimesheetDTO | null))

      const allPromises = employees.map(e => api
        .get<TimesheetDTO[]>(`/timesheets/employee/${e.id}`)
        .then(r => r.data)
        .catch(() => [] as TimesheetDTO[]))

      const [weekly, all] = await Promise.all([
        Promise.all(weeklyPromises),
        Promise.all(allPromises)
      ])

      const hoursThisWeek = weekly.reduce((acc, ts) => acc + sumHours(ts), 0)
      const hoursToDate = all.reduce((acc, list) => acc + sumHoursMany(list), 0)

      setStats({ employeeCount, hoursThisWeek, hoursToDate, employees })
    } catch (e: any) {
      setError(e?.message ?? 'Error loading dashboard')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    load()
  }, [load])

  return { ...stats, loading, error, reload: load }
}

