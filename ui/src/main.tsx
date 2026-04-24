import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Route, Routes, Link } from 'react-router-dom';
import { CssBaseline, AppBar, Toolbar, Typography, Container, Button } from '@mui/material';
import App from './pages/App';
import Dashboard from './pages/Dashboard';
import TimesheetPage from './pages/TimesheetPage';
import ReportPage from './pages/ReportPage';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <CssBaseline />
    <BrowserRouter>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>Ralph Timesheet</Typography>
          <Button color="inherit" component={Link} to="/">Home</Button>
          <Button color="inherit" component={Link} to="/employees">Employees</Button>
          <Button color="inherit" component={Link} to="/report">Report</Button>
        </Toolbar>
      </AppBar>
      <Container sx={{ mt: 3 }}>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/employees" element={<App />} />
          <Route path="/timesheet/:employeeId" element={<TimesheetPage />} />
          <Route path="/report" element={<ReportPage />} />
        </Routes>
      </Container>
    </BrowserRouter>
  </React.StrictMode>
);
