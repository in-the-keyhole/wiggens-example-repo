import { useEffect, useMemo, useState } from 'react'
import { Alert, Paper, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography } from '@mui/material'
import api, { TimesheetDTO } from '../../api/client'
import { useLocation } from 'react-router-dom'

export default function ReportPage() {
  const location = useLocation()
  const employeeFromQuery = useMemo(() => {
    const p = new URLSearchParams(location.search).get('employeeId')
    return p ? Number(p) : 1
  }, [location.search])
  const [employeeId, setEmployeeId] = useState<number>(employeeFromQuery)
  const [rows, setRows] = useState<TimesheetDTO[]>([])
  const [error, setError] = useState<string | null>(null)

  async function load() {
    try {
      const { data } = await api.get<TimesheetDTO[]>(`/timesheets/employee/${employeeId}`)
      setRows(data)
      setError(null)
    } catch (e: any) {
      setError(e?.message ?? 'Error loading report')
    }
  }

  useEffect(() => { load() }, [])

  return (
    <Paper sx={{ p: 2 }}>
      <Typography variant="h5" gutterBottom>Timesheet Summary</Typography>
      <TextField sx={{ mb: 2 }} label="Employee ID" type="number" value={employeeId} onChange={e => setEmployeeId(Number(e.target.value))} onBlur={load} />
      {error && <Alert severity="error">{error}</Alert>}
      <Table size="small">
        <TableHead>
          <TableRow>
            <TableCell>Week Start</TableCell>
            <TableCell align="right">Total Hours</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((r) => {
            const total = r.hoursMon + r.hoursTue + r.hoursWed + r.hoursThu + r.hoursFri + r.hoursSat + r.hoursSun
            return (
              <TableRow key={`${r.employeeId}-${r.weekStart}`}>
                <TableCell>{r.weekStart}</TableCell>
                <TableCell align="right">{total}</TableCell>
              </TableRow>
            )
          })}
        </TableBody>
      </Table>
    </Paper>
  )
}
