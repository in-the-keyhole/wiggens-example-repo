import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import TimesheetPage from './index'

vi.mock('../../shared/codex-example/api/timesheets', () => ({
  listEmployees: () => Promise.resolve([{ id: 1, name: 'Alice', email: 'alice@example.com' }]),
  upsertTimesheet: () => Promise.resolve({})
}))

describe('TimesheetPage', () => {
  it('renders and saves', async () => {
    render(<TimesheetPage />)
    expect(await screen.findByLabelText(/Employee/i)).toBeInTheDocument()
    fireEvent.change(screen.getByLabelText(/Employee/i), { target: { value: '1' } })
    fireEvent.click(screen.getByRole('button', { name: /save/i }))
    await waitFor(() => screen.getByText(/Saved!/i))
    expect(screen.getByText(/Saved!/i)).toBeInTheDocument()
  })
})

