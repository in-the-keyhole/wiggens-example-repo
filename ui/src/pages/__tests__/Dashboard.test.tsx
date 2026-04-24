import { describe, expect, it, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import Dashboard from '../../pages/Dashboard'
import { MemoryRouter } from 'react-router-dom'

vi.mock('../../api/client', () => ({
  fetchStats: () => Promise.resolve({ employeeCount: 1, totalHoursThisWeek: '8', totalHoursAllTime: '8' })
}))

describe('Dashboard', () => {
  it('renders stat cards', async () => {
    render(
      <MemoryRouter>
        <Dashboard />
      </MemoryRouter>
    )
    expect(await screen.findByText('Employees')).toBeDefined()
  })
})
