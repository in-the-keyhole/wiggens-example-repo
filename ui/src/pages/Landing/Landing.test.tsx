import { render, screen } from '@testing-library/react'
import Landing from '.'

describe('Landing', () => {
  it('renders welcome text', () => {
    render(<Landing />)
    expect(screen.getByText(/Welcome to the Timesheet System/i)).toBeInTheDocument()
  })
})

