import { useMemo, useState } from 'react'
import { Typography, TextField, Button, Table, TableHead, TableRow, TableCell, TableBody } from '@mui/material'
import dayjs from 'dayjs'
import { getSummary } from '../../codex-example/api/client'

export default function ReportPage() {
  const start = useMemo(() => dayjs().startOf('month').format('YYYY-MM-DD'), [])
  const end = useMemo(() => dayjs().endOf('month').format('YYYY-MM-DD'), [])
  const [from, setFrom] = useState(start)
  const [to, setTo] = useState(end)
  const [rows, setRows] = useState<{ employeeId: number; employeeName: string; totalHours: number }[]>([])

  const run = async () => {
    const data = await getSummary(from, to)
    setRows(data)
  }

  return (
    <div>
      <Typography variant="h5" gutterBottom>Summary Report</Typography>
      <div style={{ display: 'flex', gap: 12, alignItems: 'center', marginBottom: 12 }}>
        <TextField type="date" label="From" value={from} onChange={e => setFrom(e.target.value)} />
        <TextField type="date" label="To" value={to} onChange={e => setTo(e.target.value)} />
        <Button variant="contained" onClick={run}>Run</Button>
      </div>
      <Table size="small">
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
    </div>
  )
}

