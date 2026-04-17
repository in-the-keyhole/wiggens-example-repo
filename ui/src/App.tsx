import { AppBar, Box, Container, CssBaseline, Toolbar, Typography, Button } from '@mui/material'
import { Link, Outlet } from 'react-router-dom'

export default function App() {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <CssBaseline />
      <AppBar position="static" color="primary">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>Wiggens Timesheet</Typography>
          <Button color="inherit" component={Link} to="/">Dashboard</Button>
          <Button color="inherit" component={Link} to="/timesheets">Browse</Button>
          <Button color="inherit" component={Link} to="/timesheets/new">New Timesheet</Button>
        </Toolbar>
      </AppBar>
      <Container maxWidth="lg" sx={{ py: 4, flexGrow: 1 }}>
        <Outlet />
      </Container>
      <Box component="footer" sx={{ p: 2, textAlign: 'center', bgcolor: 'background.paper' }}>
        <Typography variant="caption">© Wiggens</Typography>
      </Box>
    </Box>
  )
}

