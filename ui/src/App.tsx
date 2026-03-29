import { AppBar, Box, Button, Container, CssBaseline, Toolbar, Typography } from '@mui/material'
import { Link, Route, Routes } from 'react-router-dom'
import Landing from './pages/Landing'
import TimesheetPage from './pages/Timesheet'
import DashboardPage from './pages/Dashboard'

export default function App() {
  return (
    <Box>
      <CssBaseline />
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>Timesheet</Typography>
          <Button color="inherit" component={Link} to="/">Home</Button>
          <Button color="inherit" component={Link} to="/timesheet">Timesheet</Button>
          <Button color="inherit" component={Link} to="/dashboard">Dashboard</Button>
        </Toolbar>
      </AppBar>
      <Container sx={{ mt: 3 }}>
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/timesheet" element={<TimesheetPage />} />
          <Route path="/dashboard" element={<DashboardPage />} />
        </Routes>
      </Container>
    </Box>
  )
}

