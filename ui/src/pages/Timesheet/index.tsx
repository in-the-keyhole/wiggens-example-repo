import { Box, Button, Grid, MenuItem, TextField, Typography } from '@mui/material'
import { useState } from 'react'
import dayjs from 'dayjs'
import { TimesheetItem, TimesheetRequest, upsertTimesheet } from '../../codex-example/api/client'

function startOfWeek(d: Date) {
  const day = d.getDay() // 0..6, with 0 Sun
  const diff = (day === 0 ? -6 : 1) - day // to Monday
  const monday = new Date(d)
  monday.setDate(d.getDate() + diff)
  return monday
}

export default function Timesheet() {
  const [employeeId, setEmployeeId] = useState<number>(1)
  const [weekStart, setWeekStart] = useState<string>(dayjs(startOfWeek(new Date())).format('YYYY-MM-DD'))
  const [entries, setEntries] = useState<TimesheetItem[]>(Array.from({ length: 5 }, (_, i) => ({
    workDate: dayjs(startOfWeek(new Date(weekStart))).add(i, 'day').format('YYYY-MM-DD'),
    project: 'General',
    hours: 0,
    note: ''
  })))
  const [saving, setSaving] = useState(false)
  const [result, setResult] = useState<string | null>(null)

  const handleWeekChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const ws = e.target.value
    setWeekStart(ws)
    const base = dayjs(startOfWeek(new Date(ws)))
    setEntries(entries.map((en, i) => ({ ...en, workDate: base.add(i, 'day').format('YYYY-MM-DD') })))
  }

  const updateEntry = (idx: number, patch: Partial<TimesheetItem>) => {
    setEntries(prev => prev.map((e, i) => i === idx ? { ...e, ...patch } : e))
  }

  const save = async () => {
    setSaving(true)
    setResult(null)
    try {
      const payload: TimesheetRequest = { employeeId, weekStart, entries }
      const res = await upsertTimesheet(payload)
      setResult(`Saved timesheet #${res.id} for ${res.employeeName}`)
    } catch (e: any) {
      setResult(`Error: ${e.message}`)
    } finally {
      setSaving(false)
    }
  }

  return (
    <Box>
      <Typography variant="h5" gutterBottom>Weekly Timesheet</Typography>
      <Grid container spacing={2} sx={{ mb: 2 }}>
        <Grid item xs={12} md={2}>
          <TextField label="Employee" type="number" value={employeeId} onChange={e => setEmployeeId(parseInt(e.target.value || '1', 10))} fullWidth />
        </Grid>
        <Grid item xs={12} md={3}>
          <TextField label="Week Start" type="date" value={weekStart} onChange={handleWeekChange} fullWidth InputLabelProps={{ shrink: true }} />
        </Grid>
      </Grid>
      <Grid container spacing={2}>
        {entries.map((en, i) => (
          <Grid key={i} item xs={12}>
            <Grid container spacing={2}>
              <Grid item xs={12} md={3}>
                <TextField label="Date" type="date" value={en.workDate} onChange={e => updateEntry(i, { workDate: e.target.value })} fullWidth InputLabelProps={{ shrink: true }} />
              </Grid>
              <Grid item xs={12} md={4}>
                <TextField select label="Project" value={en.project} onChange={e => updateEntry(i, { project: e.target.value })} fullWidth>
                  <MenuItem value="General">General</MenuItem>
                  <MenuItem value="ClientA">ClientA</MenuItem>
                  <MenuItem value="Internal">Internal</MenuItem>
                </TextField>
              </Grid>
              <Grid item xs={12} md={2}>
                <TextField label="Hours" type="number" inputProps={{ step: 0.5, min: 0, max: 24 }} value={en.hours} onChange={e => updateEntry(i, { hours: parseFloat(e.target.value || '0') })} fullWidth />
              </Grid>
              <Grid item xs={12} md={3}>
                <TextField label="Note" value={en.note} onChange={e => updateEntry(i, { note: e.target.value })} fullWidth />
              </Grid>
            </Grid>
          </Grid>
        ))}
      </Grid>
      <Box sx={{ mt: 2 }}>
        <Button variant="contained" onClick={save} disabled={saving}>Save</Button>
      </Box>
      {result && <Typography sx={{ mt: 2 }}>{result}</Typography>}
    </Box>
  )
}

