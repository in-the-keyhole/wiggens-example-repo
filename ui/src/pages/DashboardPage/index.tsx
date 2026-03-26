import { useEffect, useState } from 'react'
import { Card, CardContent, Stack, TextField, Typography } from '@mui/material'
import api from '../../codex-example/api/http'

type Item = { employeeId: number, employeeName: string, totalHours: number }

export default function DashboardPage() {
  const [weekStart, setWeekStart] = useState<string>(() => new Date().toISOString().slice(0,10))
  const [items, setItems] = useState<Item[]>([])

  useEffect(() => {
    (async () => {
      const { data } = await api.get('/dashboard/summary', { params: { weekStart } })
      setItems(data.items || [])
    })()
  }, [weekStart])

  return (
    <Stack spacing={2}>
      <Typography variant="h5">Weekly Dashboard</Typography>
      <TextField type="date" label="Week Start" InputLabelProps={{shrink:true}} value={weekStart} onChange={e=>setWeekStart(e.target.value)} sx={{maxWidth: 220}} />
      <Card>
        <CardContent>
          <Stack spacing={1}>
            {items.map((i) => (
              <Typography key={i.employeeId}>{i.employeeName}: {i.totalHours?.toFixed?.(2) ?? i.totalHours}h</Typography>
            ))}
            {items.length === 0 && <Typography color="text.secondary">No data</Typography>}
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )
}

