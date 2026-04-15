import { useEffect, useMemo, useState } from 'react'
import { Alert, Button, Grid, MenuItem, Paper, TextField, Typography } from '@mui/material'
import dayjs from 'dayjs'
import { listEmployees, upsertTimesheet } from '../../shared/codex-example/api/timesheets'

type Employee = { id: number, name: string, email: string }

export default function TimesheetPage() {
  const [employees, setEmployees] = useState<Employee[]>([])
  const [employeeId, setEmployeeId] = useState<number | ''>('')
  const monday = useMemo(() => dayjs().startOf('week').add(1, 'day'), [])
  const [hours, setHours] = useState({ mon: 8, tue: 8, wed: 8, thu: 8, fri: 8, sat: 0, sun: 0 })
  const [notes, setNotes] = useState('')
  const [status, setStatus] = useState<string | null>(null)

  useEffect(() => { listEmployees().then(setEmployees).catch(() => setEmployees([])) }, [])

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (employeeId === '') return
    setStatus(null)
    try {
      await upsertTimesheet({ employeeId, weekStart: monday.format('YYYY-MM-DD'), ...hours, notes })
      setStatus('Saved!')
    } catch (e) {
      setStatus('Failed to save')
    }
  }

  return (
    <Paper sx={{ p: 2 }}>
      <Typography variant="h5" gutterBottom>Enter Weekly Timesheet</Typography>
      <form onSubmit={onSubmit}>
        <Grid container spacing={2}>
          <Grid item xs={12} md={6}>
            <TextField select fullWidth label="Employee" value={employeeId} onChange={(e)=>setEmployeeId(Number(e.target.value))} required>
              {employees.map(e => <MenuItem key={e.id} value={e.id}>{e.name}</MenuItem>)}
            </TextField>
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField fullWidth label="Week Start (Mon)" value={monday.format('YYYY-MM-DD')} disabled />
          </Grid>
          {(['mon','tue','wed','thu','fri','sat','sun'] as const).map(d => (
            <Grid item xs={6} md={3} key={d}>
              <TextField type="number" inputProps={{ step: 0.5, min:0, max:24 }} label={d.toUpperCase()} fullWidth
                         value={(hours as any)[d]}
                         onChange={(e)=>setHours(h=>({...h,[d]: Number(e.target.value)}))}/>
            </Grid>
          ))}
          <Grid item xs={12}>
            <TextField fullWidth label="Notes" value={notes} onChange={e=>setNotes(e.target.value)} />
          </Grid>
          <Grid item xs={12}>
            <Button variant="contained" type="submit" disabled={employeeId===''}>Save</Button>
          </Grid>
          {status && <Grid item xs={12}><Alert severity={status==='Saved!'?'success':'error'}>{status}</Alert></Grid>}
        </Grid>
      </form>
    </Paper>
  )
}

