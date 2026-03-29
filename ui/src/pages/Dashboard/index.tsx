import { Alert, Stack, TextField, Typography } from '@mui/material'
import { useEffect, useState } from 'react'
import { getWeeklyDashboard } from '../../codex-example/api/client'

export default function DashboardPage() {
  const [weekStart, setWeekStart] = useState<string>(new Date().toISOString().slice(0,10))
  const [rows, setRows] = useState<{ employeeId: number; employeeName: string; totalHours: number }[]>([])
  const [err, setErr] = useState<string | null>(null)

  const load = async () => {
    try {
      const d = await getWeeklyDashboard(weekStart)
      setRows(d)
      setErr(null)
    } catch (e: any) {
      setErr(e?.message ?? 'Failed to load')
    }
  }

  useEffect(() => { void load() }, [weekStart])

  return (
    <Stack spacing={2}>
      <Typography variant="h5">Weekly Dashboard</Typography>
      {err && <Alert severity="error">{err}</Alert>}
      <TextField size="small" type="date" label="Week Start" InputLabelProps={{ shrink: true }} value={weekStart} onChange={e=>setWeekStart(e.target.value)} />
      <Stack>
        {rows.map(r => (
          <Typography key={r.employeeId}>{r.employeeName}: {r.totalHours.toFixed(2)} hrs</Typography>
        ))}
      </Stack>
    </Stack>
  )
}

