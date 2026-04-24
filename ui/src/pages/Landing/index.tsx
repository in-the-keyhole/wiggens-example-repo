import { Avatar, Box, Button, Card, CardActions, CardContent, Grid, MenuItem, Select, SelectChangeEvent, Typography } from '@mui/material'
import { Link, useNavigate } from 'react-router-dom'
import { useDashboard } from '../../hooks/useDashboard'
import { useMemo, useState } from 'react'

export default function Landing() {
  const { employeeCount, hoursThisWeek, hoursToDate, employees, loading, error } = useDashboard()
  const [selectedEmployee, setSelectedEmployee] = useState<string>('')
  const nav = useNavigate()

  const statCards = useMemo(() => ([
    { label: 'Employees', value: employeeCount.toString(), color: 'linear-gradient(135deg,#00BCD4,#3F51B5)' },
    { label: 'Hours This Week', value: hoursThisWeek.toFixed(1), color: 'linear-gradient(135deg,#8E24AA,#FF4081)' },
    { label: 'Hours To Date', value: hoursToDate.toFixed(1), color: 'linear-gradient(135deg,#43A047,#FFC107)' },
  ]), [employeeCount, hoursThisWeek, hoursToDate])

  const handleCreate = () => {
    const id = Number(selectedEmployee)
    if (id) nav(`/timesheet?employeeId=${id}`)
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>Dashboard</Typography>
      {error && <Typography color="error" sx={{ mb: 2 }}>{error}</Typography>}
      <Grid container spacing={2}>
        {statCards.map((c) => (
          <Grid item xs={12} md={4} key={c.label}>
            <Card sx={{ background: c.color, color: 'white' }}>
              <CardContent>
                <Typography variant="overline">{c.label}</Typography>
                <Typography variant="h4">{loading ? '…' : c.value}</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      <Grid container spacing={2} sx={{ mt: 2 }}>
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Browse Timesheets</Typography>
              <Typography variant="body2">View timesheets by employee and week.</Typography>
            </CardContent>
            <CardActions>
              <Button component={Link} to="/report" variant="outlined">Open Report</Button>
            </CardActions>
          </Card>
        </Grid>
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Create Timesheet</Typography>
              <Typography variant="body2" sx={{ mb: 1 }}>Select an employee to prefill the form.</Typography>
              <Select
                fullWidth
                size="small"
                displayEmpty
                value={selectedEmployee}
                onChange={(e: SelectChangeEvent) => setSelectedEmployee(e.target.value)}
                renderValue={(v) => v && Array.isArray(employees)
                  ? employees.find(e => String(e.id) === v)?.name
                  : 'Select Employee'}
              >
                {Array.isArray(employees) && employees.map(e => (
                  <MenuItem key={e.id} value={String(e.id)}>{e.name} (#{e.id})</MenuItem>
                ))}
              </Select>
            </CardContent>
            <CardActions>
              <Button onClick={handleCreate} variant="contained" disabled={!selectedEmployee}>New Timesheet</Button>
            </CardActions>
          </Card>
        </Grid>
      </Grid>
    </Box>
  )
}
