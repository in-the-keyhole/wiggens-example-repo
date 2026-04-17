import { useEffect, useMemo, useState } from 'react'
import { Box, Button, Grid, MenuItem, Paper, TextField, Typography } from '@mui/material'
import { EmployeesApi, TimesheetEntry, TimesheetsApi } from '../codex-example/api/client'

export default function NewTimesheet() {
  const [employees, setEmployees] = useState<{ id: number; firstName: string; lastName: string }[]>([])
  const [employeeId, setEmployeeId] = useState<number | ''>('')
  const [weekStart, setWeekStart] = useState<string>('')
  const [entries, setEntries] = useState<TimesheetEntry[]>(Array.from({ length: 5 }, (_, i) => ({ dayOfWeek: i + 1, hours: 0 } as TimesheetEntry)))
  const total = useMemo(() => entries.reduce((s, e) => s + (Number(e.hours) || 0), 0), [entries])

  useEffect(() => { (async () => setEmployees(await EmployeesApi.list()))() }, [])

  const submit = async () => {
    if (!employeeId || !weekStart) return
    await TimesheetsApi.create({ employeeId, weekStart, entries })
    setEmployeeId('')
    setWeekStart('')
    setEntries(entries.map(e => ({ ...e, hours: 0 })))
    alert('Timesheet created')
  }

  return (
    <Paper sx={{ p: 3 }}>
      <Typography variant="h5" gutterBottom>New Timesheet</Typography>
      <Grid container spacing={2}>
        <Grid item xs={12} md={6}>
          <TextField select fullWidth label="Employee" value={employeeId}
                     onChange={(e) => setEmployeeId(Number(e.target.value))}>
            {employees.map(e => (
              <MenuItem key={e.id} value={e.id}>{e.firstName} {e.lastName}</MenuItem>
            ))}
          </TextField>
        </Grid>
        <Grid item xs={12} md={6}>
          <TextField fullWidth type="date" label="Week Start" InputLabelProps={{ shrink: true }} value={weekStart}
                     onChange={(e) => setWeekStart(e.target.value)} />
        </Grid>
        {entries.map((entry, idx) => (
          <Grid item xs={12} md={4} key={idx}>
            <TextField fullWidth type="number" label={`Day ${entry.dayOfWeek} Hours`} inputProps={{ min: 0, step: 0.5 }}
                       value={entry.hours}
                       onChange={(e) => setEntries(prev => prev.map((p, i) => i === idx ? { ...p, hours: Number(e.target.value) } : p))} />
          </Grid>
        ))}
        <Grid item xs={12}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <Typography variant="subtitle1">Total Hours: {total}</Typography>
            <Button variant="contained" onClick={submit} disabled={!employeeId || !weekStart}>Create</Button>
          </Box>
        </Grid>
      </Grid>
    </Paper>
  )
}

