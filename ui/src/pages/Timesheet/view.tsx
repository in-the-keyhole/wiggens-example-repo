import { useEffect, useMemo, useState } from 'react'
import { Typography, TextField, Grid, Button, MenuItem } from '@mui/material'
import dayjs from 'dayjs'
import { Employee, Timesheet, listEmployees, submitTimesheet } from '../../codex-example/api/client'

export default function TimesheetPage() {
  const [employees, setEmployees] = useState<Employee[]>([])
  const [selected, setSelected] = useState<number | ''>('')
  const monday = useMemo(() => dayjs().startOf('week').add(1, 'day'), [])
  const [hours, setHours] = useState<Record<string, number>>({ mon: 8, tue: 8, wed: 8, thu: 8, fri: 4, sat: 0, sun: 0 })
  const [message, setMessage] = useState('')

  useEffect(() => { listEmployees().then(setEmployees).catch(() => setEmployees([])) }, [])

  const onSubmit = async () => {
    if (!selected) return
    const payload: Timesheet = {
      employeeId: selected as number,
      weekStart: monday.format('YYYY-MM-DD'),
      mon: hours.mon, tue: hours.tue, wed: hours.wed, thu: hours.thu, fri: hours.fri, sat: hours.sat, sun: hours.sun
    }
    const res = await submitTimesheet(payload)
    setMessage(`Saved. Total hours: ${res.total}`)
  }

  return (
    <div>
      <Typography variant="h5" gutterBottom>Enter Weekly Timesheet</Typography>
      <TextField
        select fullWidth label="Employee" value={selected}
        onChange={(e) => setSelected(Number(e.target.value))} sx={{ mb: 2 }}>
        {employees.map(e => <MenuItem key={e.id} value={e.id}>{e.name} ({e.email})</MenuItem>)}
      </TextField>
      <Typography variant="subtitle1">Week of {monday.format('YYYY-MM-DD')}</Typography>
      <Grid container spacing={2} sx={{ my: 1 }}>
        {['mon','tue','wed','thu','fri','sat','sun'].map(d => (
          <Grid item xs={6} sm={3} md={2} key={d}>
            <TextField type="number" inputProps={{ step: 0.5, min: 0, max: 24 }}
              label={d.toUpperCase()} value={hours[d]}
              onChange={(e) => setHours({ ...hours, [d]: Number(e.target.value) })} fullWidth />
          </Grid>
        ))}
      </Grid>
      <Button variant="contained" onClick={onSubmit} disabled={!selected}>Save</Button>
      {message && <Typography sx={{ mt: 2 }}>{message}</Typography>}
    </div>
  )
}

