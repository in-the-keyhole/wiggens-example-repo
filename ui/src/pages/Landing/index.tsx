import { Button, Stack, Typography } from '@mui/material'
import { Link as RouterLink } from 'react-router-dom'

export default function Landing() {
  return (
    <Stack spacing={2}>
      <Typography variant="h4">Welcome to the Timesheet System</Typography>
      <Typography>Select an option to get started:</Typography>
      <Stack direction="row" spacing={2}>
        <Button variant="contained" component={RouterLink} to="/timesheet">Edit Timesheet</Button>
        <Button variant="outlined" component={RouterLink} to="/dashboard">View Dashboard</Button>
      </Stack>
    </Stack>
  )
}

