import { Box, Button, MenuItem, Stack, TextField, Typography } from '@mui/material'
import dayjs from 'dayjs'
import { useEffect, useMemo, useState } from 'react'
import { Employee, listEmployees, listTimesheetsByEmployee, saveTimesheet, Timesheet, TimesheetEntry } from '../api/client'

function monday(d: Date) {
  const day = d.getDay() || 7
  const monday = new Date(d)
  monday.setDate(d.getDate() - (day - 1))
  monday.setHours(0,0,0,0)
  return monday
}

export default function Timesheets() {
  const [employees, setEmployees] = useState<Employee[]>([])
  const [employeeId, setEmployeeId] = useState<number | ''>('')
  const [weekStart, setWeekStart] = useState<string>(dayjs(monday(new Date())).format('YYYY-MM-DD'))
  const [entries, setEntries] = useState<TimesheetEntry[]>(() => Array.from({ length: 7 }, (_, i) => ({ date: dayjs(monday(new Date())).add(i, 'day').format('YYYY-MM-DD'), hours: 0 })))
  const [existing, setExisting] = useState<Timesheet[] | null>(null)

  useEffect(() => { listEmployees().then(setEmployees) }, [])
  useEffect(() => {
    if (employeeId) listTimesheetsByEmployee(employeeId).then(setExisting)
  }, [employeeId])

  useEffect(() => {
    const base = dayjs(weekStart)
    setEntries(Array.from({ length: 7 }, (_, i) => ({ date: base.add(i, 'day').format('YYYY-MM-DD'), hours: 0 })))
  }, [weekStart])

  const total = useMemo(() => entries.reduce((a, b) => a + (Number(b.hours) || 0), 0), [entries])

  const save = async () => {
    if (!employeeId) return
    const ts: Timesheet = { employeeId: Number(employeeId), weekStart, entries: entries.map(e => ({ ...e, hours: Number(e.hours) })) }
    await saveTimesheet(ts)
    if (employeeId) listTimesheetsByEmployee(employeeId).then(setExisting)
  }

  return (
    <Stack spacing={2}>
      <Typography variant="h6">Create / Update Timesheet</Typography>
      <Stack direction="row" spacing={2}>
        <TextField select label="Employee" value={employeeId} onChange={(e) => setEmployeeId(Number(e.target.value))} sx={{ minWidth: 240 }}>
          {employees.map(e => <MenuItem key={e.id} value={e.id!}>{e.firstName} {e.lastName}</MenuItem>)}
        </TextField>
        <TextField type="date" label="Week Start" value={weekStart} onChange={e => setWeekStart(e.target.value)} />
        <Box sx={{ flexGrow: 1 }} />
        <Button variant="contained" onClick={save} disabled={!employeeId}>Save</Button>
      </Stack>
      <Stack direction="row" spacing={1}>
        {entries.map((e, idx) => (
          <TextField key={idx} type="number" label={dayjs(e.date).format('ddd')} value={e.hours}
                     onChange={(ev) => setEntries(entries.map((x, i) => i === idx ? { ...x, hours: Number(ev.target.value) } : x))} />
        ))}
        <TextField label="Total" value={total} disabled />
      </Stack>

      <Typography variant="h6">Existing Timesheets</Typography>
      <ul>
        {(existing ?? []).map(ts => (
          <li key={ts.id}>{ts.weekStart} — {ts.entries?.reduce((a, b) => a + (Number(b.hours) || 0), 0)} hours</li>
        ))}
      </ul>
    </Stack>
  )
}

