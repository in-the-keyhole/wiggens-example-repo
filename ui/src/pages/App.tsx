import { AppBar, Box, Container, CssBaseline, Toolbar, Typography, Button } from '@mui/material'
import { Outlet, Link as RouterLink } from 'react-router-dom'

export default function App() {
  return (
    <>
      <CssBaseline />
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>Timesheet</Typography>
          <Button color="inherit" component={RouterLink} to="/">Home</Button>
          <Button color="inherit" component={RouterLink} to="/timesheet">Timesheet</Button>
          <Button color="inherit" component={RouterLink} to="/dashboard">Dashboard</Button>
        </Toolbar>
      </AppBar>
      <Container>
        <Box sx={{ my: 4 }}>
          <Outlet />
        </Box>
      </Container>
    </>
  )
}

