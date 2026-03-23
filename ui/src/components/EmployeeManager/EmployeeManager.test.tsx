import { fireEvent, render, screen, waitFor } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import { EmployeeManager } from './index'

const addEmployeeMock = vi.fn().mockResolvedValue(undefined)

vi.mock('../../hooks/useEmployees', () => ({
  useEmployees: () => ({
    employees: [],
    loading: false,
    error: null,
    addEmployee: addEmployeeMock,
    reload: vi.fn()
  })
}))

describe('EmployeeManager', () => {
  it('submits employee details and resets the form', async () => {
    render(<EmployeeManager />)

    fireEvent.change(screen.getByLabelText(/first name/i), { target: { value: 'Jane' } })
    fireEvent.change(screen.getByLabelText(/last name/i), { target: { value: 'Doe' } })
    fireEvent.change(screen.getByLabelText(/email/i), { target: { value: 'jane@example.com' } })

    fireEvent.click(screen.getByRole('button', { name: /save employee/i }))

    await waitFor(() => {
      expect(addEmployeeMock).toHaveBeenCalledWith({
        firstName: 'Jane',
        lastName: 'Doe',
        email: 'jane@example.com',
        active: true
      })
      expect(screen.getByLabelText(/first name/i)).toHaveValue('')
    })
  })
})
