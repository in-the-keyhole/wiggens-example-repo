import { useMemo, useState } from 'react'
import { Alert, Button, Paper, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography } from '@mui/material'
import dayjs from 'dayjs'
import { summaryReport } from '../../shared/codex-example/api/timesheets'

type Row = { employeeId: number, employeeName: string, totalHours: number }

export default function ReportsPage() {
  const [rows, setRows] = useState<Row[]>([])
  const [error, setError] = useState<string | null>(null)
  const monday = useMemo(() => dayjs().startOf('week').add(1,'day').format('YYYY-MM-DD'), [])
  const friday = useMemo(() => dayjs().startOf('week').add(5,'day').format('YYYY-MM-DD'), [])
  const [from, setFrom] = useState(monday)
  const [to, setTo] = useState(friday)

  const run = async () => {
    setError(null)
    try {
      const r = await summaryReport(from, to)
      setRows(r)
    } catch (e) {
      setError('Failed to load')
    }
  }

  return (
    <Paper sx={{ p: 2 }}>
      <Typography variant="h5" gutterBottom>Summary Report</Typography>
      <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
        <TextField type="date" label="From" value={from} onChange={e=>setFrom(e.target.value)} />
        <TextField type="date" label="To" value={to} onChange={e=>setTo(e.target.value)} />
        <Button variant="contained" onClick={run}>Run</Button>
      </div>
      {error && <Alert severity="error">{error}</Alert>}
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Employee</TableCell>
            <TableCell align="right">Total Hours</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map(r => (
            <TableRow key={r.employeeId}>
              <TableCell>{r.employeeName}</TableCell>
              <TableCell align="right">{r.totalHours}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  )
}

