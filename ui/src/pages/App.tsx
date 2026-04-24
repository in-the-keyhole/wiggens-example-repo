import { AppBar, Box, Button, Container, CssBaseline, Toolbar, Typography, ThemeProvider, createTheme } from '@mui/material'
import { Link, Outlet } from 'react-router-dom'

export default function App() {
  const theme = createTheme({
    palette: {
      mode: 'light',
      primary: { main: '#673ab7' },
      secondary: { main: '#ff4081' },
    },
    typography: {
      fontFamily: 'Inter, Roboto, Helvetica, Arial, sans-serif',
    },
    components: {
      MuiButton: { defaultProps: { variant: 'contained' } },
    },
  })
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AppBar position="static" color="primary">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Ralph Timesheet
          </Typography>
          <Button color="secondary" component={Link} to="/">Dashboard</Button>
          <Button color="secondary" component={Link} to="/employees">Employees</Button>
          <Button color="secondary" component={Link} to="/timesheets">Timesheets</Button>
        </Toolbar>
      </AppBar>
      <Container sx={{ mt: 3 }}>
        <Box>
          <Outlet />
        </Box>
      </Container>
    </ThemeProvider>
  )
}
