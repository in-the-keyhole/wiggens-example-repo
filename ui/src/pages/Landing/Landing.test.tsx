import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import Landing from '.'

describe('Landing', () => {
  it('renders heading', () => {
    render(<Landing />)
    expect(screen.getByText(/Welcome to Timesheet/i)).toBeInTheDocument()
  })
})

