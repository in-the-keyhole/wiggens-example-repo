import { Box, Button, Grid, TextField, Typography } from '@mui/material'
import { useState } from 'react'
import dayjs from 'dayjs'
import { getSummary } from '../../codex-example/api/client'

export default function Dashboard() {
  const [from, setFrom] = useState<string>(dayjs().startOf('month').format('YYYY-MM-DD'))
  const [to, setTo] = useState<string>(dayjs().endOf('month').format('YYYY-MM-DD'))
  const [report, setReport] = useState<Record<string, number>>({})

  const load = async () => {
    const res = await getSummary(from, to)
    setReport(res.totalsByEmployee || {})
  }

  return (
    <Box>
      <Typography variant="h5" gutterBottom>Summary Report</Typography>
      <Grid container spacing={2} sx={{ mb: 2 }}>
        <Grid item xs={12} md={3}>
          <TextField label="From" type="date" value={from} onChange={e => setFrom(e.target.value)} fullWidth InputLabelProps={{ shrink: true }} />
        </Grid>
        <Grid item xs={12} md={3}>
          <TextField label="To" type="date" value={to} onChange={e => setTo(e.target.value)} fullWidth InputLabelProps={{ shrink: true }} />
        </Grid>
        <Grid item xs={12} md={3}>
          <Button variant="contained" onClick={load} sx={{ mt: { xs: 2, md: 0 } }}>Load</Button>
        </Grid>
      </Grid>
      <Box>
        {Object.keys(report).length === 0 && <Typography>No data</Typography>}
        {Object.entries(report).map(([name, hours]) => (
          <Typography key={name}>{name}: {hours.toFixed(2)} hours</Typography>
        ))}
      </Box>
    </Box>
  )
}

