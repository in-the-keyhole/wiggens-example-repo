import { Box, Button, Card, CardContent, Grid, Stack, Typography } from '@mui/material'
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { fetchStats, Stats } from '../api/client'

export default function Dashboard() {
  const [stats, setStats] = useState<Stats | null>(null)
  useEffect(() => { fetchStats().then(setStats).catch(() => setStats(null)) }, [])

  return (
    <>
      <Grid container spacing={2} sx={{ mb: 2 }}>
        <Grid item xs={12} md={4}>
          <StatCard title="Employees" value={stats?.employeeCount ?? 0} />
        </Grid>
        <Grid item xs={12} md={4}>
          <StatCard title="Hours This Week" value={stats?.totalHoursThisWeek ?? '0'} />
        </Grid>
        <Grid item xs={12} md={4}>
          <StatCard title="Hours All Time" value={stats?.totalHoursAllTime ?? '0'} />
        </Grid>
      </Grid>

      <Box>
        <Stack direction="row" spacing={2}>
          <Button component={Link} to="/timesheets" color="primary">Browse Timesheets</Button>
          <Button component={Link} to="/timesheets" color="secondary">Create Timesheet</Button>
        </Stack>
      </Box>
    </>
  )
}

function StatCard({ title, value }: { title: string; value: number | string }) {
  return (
    <Card>
      <CardContent>
        <Typography color="text.secondary" gutterBottom>
          {title}
        </Typography>
        <Typography variant="h4">{value}</Typography>
      </CardContent>
    </Card>
  )
}
