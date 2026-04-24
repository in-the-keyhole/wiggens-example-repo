import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import '@testing-library/jest-dom';
import App from '.';

vi.mock('../../codex-example/api', () => ({
  EmployeesApi: { list: async () => [] },
}));

it('renders Employees header', async () => {
  render(<App />);
  expect(await screen.findByText('Employees')).toBeInTheDocument();
});
