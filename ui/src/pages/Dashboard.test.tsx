import { render, screen } from '@testing-library/react'
import Dashboard from './Dashboard'
import * as api from '../codex-example/api/client'

vi.spyOn(api.EmployeesApi, 'list').mockResolvedValue([])
vi.spyOn(api.ReportsApi, 'summary').mockResolvedValue({ weekStart: '2026-01-05', totalHoursThisWeek: 0, totalHoursAllTime: 0 })

it('renders dashboard summary cards', async () => {
  render(<Dashboard />)
  expect(await screen.findByText(/Employees/i)).toBeInTheDocument()
  expect(await screen.findByText(/Hours \(Week starting/i)).toBeInTheDocument()
  expect(await screen.findByText(/Total Hours \(All Time\)/i)).toBeInTheDocument()
})

