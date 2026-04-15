import { useEffect, useMemo, useState } from 'react'
import { Box, Button, Card, CardContent, Grid, MenuItem, Select, Tab, Tabs, TextField, Typography } from '@mui/material'
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
  const [tab, setTab] = useState<'dashboard' | 'add' | 'reports'>('dashboard')
  const [weeklyTotal, setWeeklyTotal] = useState<number>(0)
  const [toDateTotal, setToDateTotal] = useState<number>(0)

  const weekStart = useMemo(() => startOfWeek(), [])

  useEffect(() => {
    listEmployees()
      .then((data) => {
        // Guard against non-array responses (e.g., dev server HTML fallback)
        setEmployees(Array.isArray(data) ? data : [])
      })
      .catch(() => setEmployees([]))

    // Load dashboard metrics
    const start = weekStart
    const end = dayjs(weekStart).add(6, 'day').format('YYYY-MM-DD')
    getSummary(start, end)
      .then((rows) => setWeeklyTotal(rows.reduce((sum: number, r: any) => sum + Number(r.totalHours || 0), 0)))
      .catch(() => setWeeklyTotal(0))

    // To-date: from an early epoch to today
    const epoch = '2000-01-01'
    const today = dayjs().format('YYYY-MM-DD')
    getSummary(epoch, today)
      .then((rows) => setToDateTotal(rows.reduce((sum: number, r: any) => sum + Number(r.totalHours || 0), 0)))
      .catch(() => setToDateTotal(0))
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

      <Tabs value={tab} onChange={(_, v) => setTab(v)} sx={{ mb: 2 }}>
        <Tab label="Dashboard" value="dashboard" />
        <Tab label="Add Timesheet" value="add" />
        <Tab label="Reporting Center" value="reports" />
      </Tabs>

      {tab === 'dashboard' && (
        <>
          <Grid container spacing={2} sx={{ mb: 2 }}>
            <Grid item xs={12} md={4}>
              <Card>
                <CardContent>
                  <Typography variant="subtitle2" color="text.secondary">Employees</Typography>
                  <Typography variant="h5">{employees.length}</Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={4}>
              <Card>
                <CardContent>
                  <Typography variant="subtitle2" color="text.secondary">Hours This Week</Typography>
                  <Typography variant="h5">{weeklyTotal}</Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={4}>
              <Card>
                <CardContent>
                  <Typography variant="subtitle2" color="text.secondary">Hours To Date</Typography>
                  <Typography variant="h5">{toDateTotal}</Typography>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Quick Actions</Typography>
              <Grid container spacing={2}>
                <Grid item xs={12} md={4}>
                  <Button fullWidth variant="contained" onClick={() => setTab('add')}>Add Timesheet</Button>
                </Grid>
                <Grid item xs={12} md={4}>
                  <Button fullWidth variant="outlined" onClick={handleCreateEmployee}>Add Employee</Button>
                </Grid>
                <Grid item xs={12} md={4}>
                  <Button fullWidth variant="outlined" onClick={() => setTab('reports')}>Open Reporting Center</Button>
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </>
      )}

      {tab === 'add' && (
        <Card>
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
      )}

      {tab === 'reports' && (
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
      )}
    </Box>
  )
}
