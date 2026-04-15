import { render, screen } from '@testing-library/react'
import App from './index'

vi.mock('../../codex-example/api/client', () => ({
  listEmployees: async () => [],
  createEmployee: async (e:any) => ({ id: 1, ...e }),
  createTimesheet: async () => ({ id: 1, totalHours: 16, employeeId: 1, weekStart: '2024-01-01', entries: [] }),
  getSummary: async () => []
}))

describe('App', () => {
  it('renders title', () => {
    render(<App />)
    expect(screen.getByText(/Ralph Timesheet/i)).toBeInTheDocument()
  })
})

