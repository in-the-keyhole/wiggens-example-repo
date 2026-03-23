import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import CssBaseline from '@mui/material/CssBaseline'
import { Layout } from './components/Layout'
import { EmployeeManager } from './components/EmployeeManager'
import { ProjectManager } from './components/ProjectManager'
import { TimesheetManager } from './components/TimesheetManager'
import { ReportDashboard } from './components/ReportDashboard'

function App() {
  return (
    <BrowserRouter>
      <CssBaseline />
      <Layout>
        <Routes>
          <Route path="/" element={<EmployeeManager />} />
          <Route path="/projects" element={<ProjectManager />} />
          <Route path="/timesheets" element={<TimesheetManager />} />
          <Route path="/reports" element={<ReportDashboard />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  )
}

export default App
