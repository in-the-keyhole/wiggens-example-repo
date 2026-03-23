import { Link as RouterLink } from 'react-router-dom'
import AppBar from '@mui/material/AppBar'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import Container from '@mui/material/Container'
import Stack from '@mui/material/Stack'
import Toolbar from '@mui/material/Toolbar'
import Typography from '@mui/material/Typography'
import type { PropsWithChildren } from 'react'

const navItems = [
  { label: 'Employees', to: '/' },
  { label: 'Projects', to: '/projects' },
  { label: 'Timesheets', to: '/timesheets' },
  { label: 'Reports', to: '/reports' }
]

export const Layout = ({ children }: PropsWithChildren) => (
  <Box sx={{ minHeight: '100vh', bgcolor: (theme) => theme.palette.grey[100] }}>
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          Timesheet System
        </Typography>
        <Stack direction="row" spacing={1}>
          {navItems.map((item) => (
            <Button
              key={item.to}
              color="inherit"
              component={RouterLink}
              to={item.to}
            >
              {item.label}
            </Button>
          ))}
        </Stack>
      </Toolbar>
    </AppBar>
    <Container sx={{ py: 4 }} maxWidth="lg">
      {children}
    </Container>
  </Box>
)
