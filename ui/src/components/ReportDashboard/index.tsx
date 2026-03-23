import { FormEvent, useMemo, useState } from 'react'
import Alert from '@mui/material/Alert'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import Paper from '@mui/material/Paper'
import Stack from '@mui/material/Stack'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import TextField from '@mui/material/TextField'
import Typography from '@mui/material/Typography'
import MenuItem from '@mui/material/MenuItem'
import { useEmployees } from '../../hooks/useEmployees'
import { useEmployeeReport } from '../../hooks/useEmployeeReport'

interface ReportFilters {
  employeeId: string
  startDate?: string
  endDate?: string
}

const todayIso = (): string => new Date().toISOString().split('T')[0]

const defaultFilters: ReportFilters = {
  employeeId: '',
  startDate: todayIso(),
  endDate: todayIso()
}

export const ReportDashboard = () => {
  const { employees } = useEmployees()
  const [formState, setFormState] = useState<ReportFilters>(defaultFilters)
  const [filters, setFilters] = useState<ReportFilters>(defaultFilters)

  const { report, loading, error } = useEmployeeReport(filters)

  const totalHours = useMemo(() => report?.projectHours.reduce((sum, item) => sum + Number(item.hours), 0) ?? 0, [report])

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setFilters(formState)
  }

  return (
    <Stack spacing={4}>
      <Paper component="form" onSubmit={handleSubmit} sx={{ p: 3 }} elevation={1}>
        <Stack spacing={2}>
          <Typography variant="h6">Employee Hours Report</Typography>
          <TextField
            select
            label="Employee"
            value={formState.employeeId}
            onChange={(event) => setFormState((prev) => ({ ...prev, employeeId: event.target.value }))}
            required
            fullWidth
          >
            <MenuItem value="" disabled>
              Select employee
            </MenuItem>
            {employees.map((employee) => (
              <MenuItem key={employee.id} value={employee.id}>
                {employee.firstName} {employee.lastName}
              </MenuItem>
            ))}
          </TextField>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
            <TextField
              label="Start Date"
              type="date"
              value={formState.startDate ?? ''}
              onChange={(event) => setFormState((prev) => ({ ...prev, startDate: event.target.value }))}
              InputLabelProps={{ shrink: true }}
              fullWidth
            />
            <TextField
              label="End Date"
              type="date"
              value={formState.endDate ?? ''}
              onChange={(event) => setFormState((prev) => ({ ...prev, endDate: event.target.value }))}
              InputLabelProps={{ shrink: true }}
              fullWidth
            />
          </Stack>
          <Box>
            <Button type="submit" variant="contained" disabled={!formState.employeeId}>
              Run Report
            </Button>
          </Box>
        </Stack>
      </Paper>

      <Paper sx={{ p: 3 }} elevation={1}>
        {error && <Alert severity="error">{error}</Alert>}
        {!filters.employeeId ? (
          <Typography>Select an employee to view report results.</Typography>
        ) : loading ? (
          <Typography>Loading report...</Typography>
        ) : report ? (
          <Stack spacing={2}>
            <Typography variant="h6">
              {report.employeeName} · {report.startDate} to {report.endDate}
            </Typography>
            <Typography variant="subtitle1">Total Hours: {totalHours.toFixed(2)}</Typography>
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell>Project</TableCell>
                  <TableCell align="right">Hours</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {report.projectHours.map((item) => (
                  <TableRow key={item.projectId}>
                    <TableCell>{item.projectCode} · {item.projectName}</TableCell>
                    <TableCell align="right">{Number(item.hours).toFixed(2)}</TableCell>
                  </TableRow>
                ))}
                {report.projectHours.length === 0 && (
                  <TableRow>
                    <TableCell colSpan={2}>No hours logged for this range.</TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </Stack>
        ) : (
          <Typography>No data available. Try adjusting your filters.</Typography>
        )}
      </Paper>
    </Stack>
  )
}
