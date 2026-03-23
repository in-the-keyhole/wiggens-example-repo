import { FormEvent, useState } from 'react'
import Alert from '@mui/material/Alert'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import FormControlLabel from '@mui/material/FormControlLabel'
import Grid from '@mui/material/Unstable_Grid2'
import Paper from '@mui/material/Paper'
import Stack from '@mui/material/Stack'
import Switch from '@mui/material/Switch'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import TextField from '@mui/material/TextField'
import Typography from '@mui/material/Typography'
import { useEmployees } from '../../hooks/useEmployees'

export const EmployeeManager = () => {
  const { employees, addEmployee, error, loading } = useEmployees()
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')
  const [email, setEmail] = useState('')
  const [active, setActive] = useState(true)
  const [submitting, setSubmitting] = useState(false)

  const resetForm = () => {
    setFirstName('')
    setLastName('')
    setEmail('')
    setActive(true)
  }

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setSubmitting(true)
    try {
      await addEmployee({ firstName, lastName, email, active })
      resetForm()
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <Stack spacing={4}>
      <Paper sx={{ p: 3 }} elevation={1} component="form" onSubmit={handleSubmit}>
        <Typography variant="h6" gutterBottom>
          Add Employee
        </Typography>
        <Grid container spacing={2}>
          <Grid xs={12} sm={6}>
            <TextField
              label="First Name"
              value={firstName}
              onChange={(event) => setFirstName(event.target.value)}
              required
              fullWidth
            />
          </Grid>
          <Grid xs={12} sm={6}>
            <TextField
              label="Last Name"
              value={lastName}
              onChange={(event) => setLastName(event.target.value)}
              required
              fullWidth
            />
          </Grid>
          <Grid xs={12}>
            <TextField
              label="Email"
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              required
              fullWidth
            />
          </Grid>
          <Grid xs={12}>
            <FormControlLabel
              control={<Switch checked={active} onChange={(event) => setActive(event.target.checked)} />}
              label="Active"
            />
          </Grid>
        </Grid>
        <Box mt={2}>
          <Button type="submit" variant="contained" disabled={submitting}>
            Save Employee
          </Button>
        </Box>
        {error && (
          <Box mt={2}>
            <Alert severity="error">{error}</Alert>
          </Box>
        )}
      </Paper>

      <Paper sx={{ p: 3 }} elevation={1}>
        <Typography variant="h6" gutterBottom>
          Employees
        </Typography>
        {loading ? (
          <Typography>Loading employees...</Typography>
        ) : (
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Active</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {employees.map((employee) => (
                <TableRow key={employee.id}>
                  <TableCell>{`${employee.firstName} ${employee.lastName}`}</TableCell>
                  <TableCell>{employee.email}</TableCell>
                  <TableCell>{employee.active ? 'Yes' : 'No'}</TableCell>
                </TableRow>
              ))}
              {employees.length === 0 && (
                <TableRow>
                  <TableCell colSpan={3}>No employees found.</TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        )}
      </Paper>
    </Stack>
  )
}
