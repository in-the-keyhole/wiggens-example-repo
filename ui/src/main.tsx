import React from 'react'
import ReactDOM from 'react-dom/client'
import { CssBaseline, ThemeProvider, createTheme } from '@mui/material'
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import App from './App'
import Landing from './pages/Landing'
import TimesheetPage from './pages/Timesheet'
import ReportPage from './pages/Report'

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#7C4DFF' },
    secondary: { main: '#00BCD4' },
    background: { default: '#fafafa' }
  },
  shape: { borderRadius: 10 },
  typography: { fontFamily: 'Inter, Roboto, Helvetica, Arial, sans-serif' }
})

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<App />}> 
            <Route index element={<Landing />} />
            <Route path="timesheet" element={<TimesheetPage />} />
            <Route path="report" element={<ReportPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </ThemeProvider>
  </React.StrictMode>
)
