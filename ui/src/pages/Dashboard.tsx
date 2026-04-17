import { Card, CardContent, Grid, Typography, CircularProgress } from '@mui/material'
import { useEffect, useState } from 'react'
import { ReportsApi, EmployeesApi } from '../codex-example/api/client'

export default function Dashboard() {
  const [loading, setLoading] = useState(true)
  const [employeeCount, setEmployeeCount] = useState<number>(0)
  const [weekStart, setWeekStart] = useState<string>('')
  const [weekHours, setWeekHours] = useState<number>(0)
  const [totalHours, setTotalHours] = useState<number>(0)

  useEffect(() => {
    (async () => {
      try {
        const [emps, summary] = await Promise.all([
          EmployeesApi.list(),
          ReportsApi.summary()
        ])
        setEmployeeCount(emps.length)
        setWeekStart(summary.weekStart)
        setWeekHours(summary.totalHoursThisWeek)
        setTotalHours(summary.totalHoursAllTime)
      } finally {
        setLoading(false)
      }
    })()
  }, [])

  if (loading) return <CircularProgress />

  return (
    <Grid container spacing={2}>
      <Grid item xs={12} md={4}>
        <Card>
          <CardContent>
            <Typography color="text.secondary" gutterBottom>Employees</Typography>
            <Typography variant="h4">{employeeCount}</Typography>
          </CardContent>
        </Card>
      </Grid>
      <Grid item xs={12} md={4}>
        <Card>
          <CardContent>
            <Typography color="text.secondary" gutterBottom>Hours (Week starting {weekStart})</Typography>
            <Typography variant="h4">{weekHours}</Typography>
          </CardContent>
        </Card>
      </Grid>
      <Grid item xs={12} md={4}>
        <Card>
          <CardContent>
            <Typography color="text.secondary" gutterBottom>Total Hours (All Time)</Typography>
            <Typography variant="h4">{totalHours}</Typography>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  )
}

