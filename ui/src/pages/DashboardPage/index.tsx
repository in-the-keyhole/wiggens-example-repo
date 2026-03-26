import { useEffect, useState } from 'react'
import { getDashboard } from '../../shared/codex-example/api/timesheets'

export default function DashboardPage() {
  const [weekStart, setWeekStart] = useState(() => new Date().toISOString().slice(0, 10))
  const [summary, setSummary] = useState<{ weekStart: string; totalHours: number } | null>(null)

  useEffect(() => {
    (async () => {
      try {
        const res = await getDashboard(weekStart)
        setSummary(res)
      } catch (e) {
        setSummary(null)
      }
    })()
  }, [weekStart])

  return (
    <div>
      <h2>Weekly Dashboard</h2>
      <div style={{ display: 'flex', gap: 12, alignItems: 'center', marginBottom: 12 }}>
        <label>
          Week Start
          <input type="date" value={weekStart} onChange={e => setWeekStart(e.target.value)} style={{ marginLeft: 8 }} />
        </label>
      </div>
      {summary ? (
        <div>
          <div>Week: {summary.weekStart}</div>
          <div>Total Hours Reported: {summary.totalHours}</div>
        </div>
      ) : (
        <div>No data yet (ensure API is running).</div>
      )}
    </div>
  )
}

