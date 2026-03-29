import { Alert, Button, FormControl, InputLabel, MenuItem, Select, Stack, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography } from '@mui/material'
import dayjs from 'dayjs'
import isoWeek from 'dayjs/plugin/isoWeek'
import { useEffect, useMemo, useState } from 'react'
import { Employee, Project, TimesheetEntry, getTimesheet, listEmployees, listProjects, upsertTimesheet } from '../../codex-example/api/client'

dayjs.extend(isoWeek)

const days = ['MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY'] as const

export default function TimesheetPage() {
  const [employees, setEmployees] = useState<Employee[]>([])
  const [projects, setProjects] = useState<Project[]>([])
  const [employeeId, setEmployeeId] = useState<number | ''>('')
  const [weekStart, setWeekStart] = useState<string>(dayjs().startOf('week').add(1,'day').format('YYYY-MM-DD'))
  const [entries, setEntries] = useState<TimesheetEntry[]>([])
  const [msg, setMsg] = useState<string | null>(null)

  useEffect(() => { (async () => {
    setEmployees(await listEmployees());
    setProjects(await listProjects());
  })() }, [])

  const load = async () => {
    if (!employeeId) return
    try {
      const ts = await getTimesheet(employeeId as number, weekStart)
      setEntries(ts.entries)
    } catch {
      setEntries([])
    }
  }

  useEffect(() => { void load() }, [employeeId, weekStart])

  const setHour = (projectId: number, day: string, hours: number) => {
    setEntries(prev => {
      const idx = prev.findIndex(e => e.projectId === projectId && e.dayOfWeek === day)
      const next = [...prev]
      if (idx >= 0) next[idx] = { ...next[idx], hours }
      else next.push({ projectId, dayOfWeek: day, hours })
      return next
    })
  }

  const total = useMemo(() => entries.reduce((s,e)=>s+e.hours,0), [entries])

  const save = async () => {
    if (!employeeId) return
    await upsertTimesheet({ employeeId: employeeId as number, weekStart, entries: entries.map(e => ({ projectId: e.projectId, dayOfWeek: e.dayOfWeek, hours: e.hours })) })
    setMsg('Timesheet saved')
    await load()
  }

  return (
    <Stack spacing={2}>
      <Typography variant="h5">Weekly Timesheet</Typography>
      {msg && <Alert onClose={()=>setMsg(null)}>{msg}</Alert>}
      <Stack direction="row" spacing={2}>
        <FormControl size="small" sx={{ minWidth: 200 }}>
          <InputLabel id="emp">Employee</InputLabel>
          <Select labelId="emp" label="Employee" value={employeeId} onChange={e=> setEmployeeId(e.target.value as number)}>
            {employees.map(e => <MenuItem key={e.id} value={e.id}>{e.firstName} {e.lastName}</MenuItem>)}
          </Select>
        </FormControl>
        <TextField size="small" type="date" label="Week Start (Mon)" InputLabelProps={{ shrink: true }} value={weekStart} onChange={e=>setWeekStart(e.target.value)} />
        <Button variant="contained" onClick={save} disabled={!employeeId}>Save</Button>
        <Typography sx={{ ml: 'auto' }}>Total: {total.toFixed(2)}</Typography>
      </Stack>
      <Table size="small">
        <TableHead>
          <TableRow>
            <TableCell>Project</TableCell>
            {days.map(d => <TableCell key={d}>{d.slice(0,3)}</TableCell>)}
          </TableRow>
        </TableHead>
        <TableBody>
          {projects.map(p => (
            <TableRow key={p.id}>
              <TableCell>{p.name}</TableCell>
              {days.map(d => {
                const val = entries.find(e => e.projectId === p.id && e.dayOfWeek === d)?.hours || 0
                return (
                  <TableCell key={d}>
                    <TextField size="small" type="number" inputProps={{ step: 0.5, min: 0 }} value={val}
                               onChange={e => setHour(p.id, d, Number(e.target.value))} />
                  </TableCell>
                )
              })}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Stack>
  )
}

