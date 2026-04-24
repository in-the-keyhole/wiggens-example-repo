import { AppBar, Box, Container, Link as MuiLink, Toolbar, Typography } from '@mui/material'
import { Link, Outlet } from 'react-router-dom'

export default function App() {
  return (
    <Box>
      <AppBar position="static" sx={{
        background: 'linear-gradient(90deg, #7C4DFF 0%, #00BCD4 100%)'
      }}>
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>Ralph Timesheet</Typography>
          <MuiLink component={Link} color="inherit" underline="none" to="/">Home</MuiLink>
          <Box sx={{ mx: 2 }} />
          <MuiLink component={Link} color="inherit" underline="none" to="/timesheet">Timesheet</MuiLink>
          <Box sx={{ mx: 2 }} />
          <MuiLink component={Link} color="inherit" underline="none" to="/report">Report</MuiLink>
          <Box sx={{ mx: 2 }} />
          <MuiLink component={Link} color="inherit" underline="none" to="/employees">Employees</MuiLink>
        </Toolbar>
      </AppBar>
      <Container sx={{ mt: 3 }}>
        <Outlet />
      </Container>
    </Box>
  )
}
