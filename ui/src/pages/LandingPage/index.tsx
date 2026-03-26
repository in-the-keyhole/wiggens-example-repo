import { Link } from 'react-router-dom'

export default function LandingPage() {
  return (
    <div>
      <h2>Welcome to the Timesheet System</h2>
      <p>Manage weekly timesheets and view reporting dashboards.</p>
      <ul>
        <li><Link to="/timesheets/edit">Add or Edit Weekly Timesheet</Link></li>
        <li><Link to="/dashboard">View Weekly Dashboard</Link></li>
      </ul>
    </div>
  )
}

