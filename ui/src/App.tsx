import { useEffect, useState } from 'react'
import { Container, Typography, TextField, Button, Stack } from '@mui/material'
import { api } from './api/client'

function App() {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [employeeId, setEmployeeId] = useState<number | null>(null)

  const createEmployee = async () => {
    const res = await api.post('/employees', { name, email })
    setEmployeeId(res.data.id)
  }

  useEffect(() => {
    // no-op bootstrap
  }, [])

  return (
    <Container maxWidth="sm" sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>Timesheet</Typography>
      <Typography variant="subtitle1" gutterBottom>Quick start: create an employee</Typography>
      <Stack spacing={2} direction="column">
        <TextField label="Name" value={name} onChange={e => setName(e.target.value)} />
        <TextField label="Email" value={email} onChange={e => setEmail(e.target.value)} />
        <Button variant="contained" onClick={createEmployee} disabled={!name || !email}>Create Employee</Button>
      </Stack>
      {employeeId && (
        <Typography sx={{ mt: 2 }}>Created Employee ID: {employeeId}</Typography>
      )}
    </Container>
  )
}

export default App

