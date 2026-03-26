import { Link, Route, Routes, Navigate } from 'react-router-dom'
import LandingPage from './pages/LandingPage'
import TimesheetEditorPage from './pages/TimesheetEditorPage'
import DashboardPage from './pages/DashboardPage'
import AppBar from '@mui/material/AppBar'
import Toolbar from '@mui/material/Toolbar'
import Typography from '@mui/material/Typography'
import Container from '@mui/material/Container'

export default function App() {
  return (
    <div>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>Timesheet</Typography>
          <nav style={{ display: 'flex', gap: 12 }}>
            <Link to="/" style={{ color: 'white' }}>Home</Link>
            <Link to="/timesheets/edit" style={{ color: 'white' }}>Edit Timesheet</Link>
            <Link to="/dashboard" style={{ color: 'white' }}>Dashboard</Link>
          </nav>
        </Toolbar>
      </AppBar>
      <Container sx={{ mt: 3 }}>
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/timesheets/edit" element={<TimesheetEditorPage />} />
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Container>
    </div>
  )
}
