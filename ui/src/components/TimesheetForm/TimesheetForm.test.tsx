import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import TimesheetForm from './index'

describe('TimesheetForm', () => {
  it('calls onSubmit with values', () => {
    const onSubmit = vi.fn()
    render(<TimesheetForm onSubmit={onSubmit} />)
    fireEvent.change(screen.getByLabelText('Employee ID'), { target: { value: '1' } })
    fireEvent.change(screen.getByLabelText('Week Start (YYYY-MM-DD)'), { target: { value: '2026-03-16' } })
    fireEvent.click(screen.getByRole('button', { name: /Create Timesheet/i }))
    expect(onSubmit).toHaveBeenCalledWith(1, '2026-03-16')
  })
})

