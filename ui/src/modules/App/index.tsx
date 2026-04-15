import { useEffect, useMemo, useState } from 'react'
import { Box, Button, Card, CardContent, Grid, MenuItem, Select, TextField, Typography } from '@mui/material'
import dayjs from 'dayjs'
import { createEmployee, createTimesheet, EntryDto, getSummary, listEmployees, type Employee } from '../../codex-example/api/client'

function startOfWeek(date = new Date()) {
  const d = dayjs(date)
  // Monday start
  const diff = (d.day() + 6) % 7
  return d.subtract(diff, 'day').format('YYYY-MM-DD')
}

export default function App() {
  const [employees, setEmployees] = useState<Employee[]>([])
  const [selectedEmployee, setSelectedEmployee] = useState<number | ''>('')
  const [project, setProject] = useState('Project A')
  const [hoursByDay, setHoursByDay] = useState<Record<string, number>>({ MONDAY: 0, TUESDAY: 0, WEDNESDAY: 0, THURSDAY: 0, FRIDAY: 0 })
  const [summary, setSummary] = useState<any[]>([])

  const weekStart = useMemo(() => startOfWeek(), [])

  useEffect(() => {
    listEmployees().then(setEmployees).catch(() => setEmployees([]))
  }, [])

  async function handleCreateEmployee() {
    const name = prompt('Employee name?') || ''
    const email = prompt('Employee email?') || ''
    if (!name || !email) return
    const emp = await createEmployee({ name, email })
    setEmployees((arr) => [...arr, emp])
  }

  async function handleSubmitTimesheet() {
    if (!selectedEmployee) return
    const entries: EntryDto[] = Object.entries(hoursByDay)
      .filter(([, h]) => h && h > 0)
      .map(([day, hours]) => ({ dayOfWeek: day, hours, project }))
    const res = await createTimesheet({ employeeId: Number(selectedEmployee), weekStart, entries })
    alert(`Saved timesheet. Total hours: ${res.totalHours}`)
  }

  async function handleLoadSummary() {
    const start = weekStart
    const end = dayjs(weekStart).add(6, 'day').format('YYYY-MM-DD')
    const data = await getSummary(start, end)
    setSummary(data)
  }

  return (
    <Box mt={4}>
      <Typography variant="h4" gutterBottom>Ralph Timesheet</Typography>

      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Typography variant="h6">Enter Weekly Timesheet</Typography>
          <Grid container spacing={2} alignItems="center" sx={{ mt: 1 }}>
            <Grid item xs={12} md={4}>
              <Select fullWidth displayEmpty value={selectedEmployee} onChange={(e) => setSelectedEmployee(e.target.value as any)}>
                <MenuItem value="" disabled>Select employee</MenuItem>
                {employees.map((e) => (
                  <MenuItem key={e.id} value={e.id}>{e.name} ({e.email})</MenuItem>
                ))}
              </Select>
            </Grid>
            <Grid item xs={12} md={3}>
              <TextField fullWidth label="Project" value={project} onChange={(e) => setProject(e.target.value)} />
            </Grid>
            {Object.keys(hoursByDay).map((day) => (
              <Grid key={day} item xs={6} md={1.6 as any}>
                <TextField type="number" inputProps={{ min: 0, max: 24, step: 0.5 }}
                  label={day.substring(0,3)} value={hoursByDay[day]} onChange={(e) => setHoursByDay({ ...hoursByDay, [day]: Number(e.target.value) })} />
              </Grid>
            ))}
            <Grid item xs={12} md={2}>
              <Button variant="contained" onClick={handleSubmitTimesheet} fullWidth>Save</Button>
            </Grid>
            <Grid item xs={12} md={2}>
              <Button variant="outlined" onClick={handleCreateEmployee} fullWidth>Add Employee</Button>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      <Card>
        <CardContent>
          <Typography variant="h6">Weekly Summary</Typography>
          <Button sx={{ mt: 1, mb: 2 }} variant="outlined" onClick={handleLoadSummary}>Load Summary</Button>
          {summary.map((s) => (
            <Box key={`${s.employeeId}-${s.weekStart}`} sx={{ mb: 1 }}>
              <Typography>{s.employeeName} — {s.weekStart}: {s.totalHours}h</Typography>
            </Box>
          ))}
        </CardContent>
      </Card>
    </Box>
  )
}

