import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import React from 'react'
import EmployeesPage from '.'

vi.mock('../../api/employees', () => {
  let data = [
    { id: 1, name: 'Alice', email: 'alice@example.com' },
    { id: 2, name: 'Bob', email: 'bob@example.com' },
  ]
  return {
    default: {
      listEmployees: vi.fn(async () => data),
      createEmployee: vi.fn(async (e: any) => ({ id: 3, ...e })),
      updateEmployee: vi.fn(async (id: number, e: any) => ({ id, ...e })),
      deleteEmployee: vi.fn(async (id: number) => { data = data.filter(d => d.id !== id) }),
    }
  }
})

describe('EmployeesPage', () => {
  beforeEach(() => {
    vi.spyOn(window, 'confirm').mockReturnValue(true)
  })

  it('renders list and can add an employee', async () => {
    render(<EmployeesPage />)
    expect(await screen.findByText('Alice')).toBeInTheDocument()

    fireEvent.click(screen.getByRole('button', { name: /add employee/i }))
    const name = await screen.findByLabelText(/name/i)
    fireEvent.change(name, { target: { value: 'Carol' } })
    const email = screen.getByLabelText(/email/i)
    fireEvent.change(email, { target: { value: 'carol@example.com' } })
    fireEvent.click(screen.getByRole('button', { name: /create/i }))

    await waitFor(() => expect(screen.getByText('Carol')).toBeInTheDocument())
  })

  it('can delete an employee', async () => {
    render(<EmployeesPage />)
    expect(await screen.findByText('Alice')).toBeInTheDocument()
    const del = await screen.findByLabelText('delete-1')
    fireEvent.click(del)
    await waitFor(() => expect(screen.queryByText('Alice')).not.toBeInTheDocument())
  })
})

