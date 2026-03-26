import { useEffect, useMemo, useState } from 'react'
import { Alert, Button, Card, CardContent, FormControl, InputLabel, MenuItem, Select, Stack, TextField, Typography } from '@mui/material'
import api from '../../codex-example/api/http'

type Entry = { dayOfWeek: number, hours: number, project?: string, notes?: string }

const weekdays = ['Mon','Tue','Wed','Thu','Fri','Sat','Sun']

export default function TimesheetPage() {
  const [employeeId, setEmployeeId] = useState<number>(1)
  const [weekStart, setWeekStart] = useState<string>(() => {
    const d = new Date();
    const day = d.getDay() || 7; // Sun => 7
    const monday = new Date(d);
    monday.setDate(d.getDate() - day + 1);
    return monday.toISOString().slice(0,10)
  })
  const [entries, setEntries] = useState<Entry[]>(() => Array.from({length:7}, (_,i)=>({dayOfWeek:i+1,hours:0})))
  const [message, setMessage] = useState<string | null>(null)

  useEffect(() => {
    (async () => {
      try {
        const { data } = await api.get(`/timesheets`, { params: { employeeId, weekStart } })
        if (data?.entries) setEntries(data.entries)
      } catch (_) {
        // ignore if not found
      }
    })()
  }, [employeeId, weekStart])

  const total = useMemo(() => entries.reduce((a,e)=>a + (Number(e.hours)||0), 0), [entries])

  const save = async () => {
    await api.post('/timesheets', { employeeId, weekStart, entries })
    setMessage('Saved!')
    setTimeout(()=>setMessage(null), 2000)
  }

  return (
    <Stack spacing={2}>
      <Typography variant="h5">Weekly Timesheet</Typography>
      {message && <Alert severity="success">{message}</Alert>}
      <Stack direction={{xs:'column', sm:'row'}} spacing={2}>
        <FormControl sx={{minWidth: 160}}>
          <InputLabel id="emp">Employee</InputLabel>
          <Select labelId="emp" value={employeeId} label="Employee" onChange={e=>setEmployeeId(Number(e.target.value))}>
            <MenuItem value={1}>Alice Adams</MenuItem>
            <MenuItem value={2}>Bob Brown</MenuItem>
            <MenuItem value={3}>Carol Clark</MenuItem>
          </Select>
        </FormControl>
        <TextField type="date" label="Week Start" InputLabelProps={{shrink:true}} value={weekStart} onChange={e=>setWeekStart(e.target.value)} />
      </Stack>

      <Card>
        <CardContent>
          <Stack spacing={2}>
            {entries.map((e, idx) => (
              <Stack key={idx} direction={{xs:'column', sm:'row'}} spacing={2} alignItems="center">
                <Typography sx={{width:60}}>{weekdays[idx]}</Typography>
                <TextField type="number" label="Hours" value={e.hours} onChange={ev=>{
                  const v = Number(ev.target.value)
                  setEntries(curr => curr.map((c,i)=> i===idx?{...c, hours: v}:c))
                }} sx={{width:120}} inputProps={{step:0.25, min:0, max:24}} />
                <TextField label="Project" value={e.project||''} onChange={ev=>setEntries(curr => curr.map((c,i)=> i===idx?{...c, project: ev.target.value}:c))} sx={{flex:1}} />
                <TextField label="Notes" value={e.notes||''} onChange={ev=>setEntries(curr => curr.map((c,i)=> i===idx?{...c, notes: ev.target.value}:c))} sx={{flex:2}} />
              </Stack>
            ))}
            <Typography>Total: {total.toFixed(2)} hours</Typography>
          </Stack>
        </CardContent>
      </Card>

      <Button variant="contained" onClick={save}>Save Timesheet</Button>
    </Stack>
  )
}

