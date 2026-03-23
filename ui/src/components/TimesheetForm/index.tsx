import { useState } from 'react'
import { TextField, Button, Stack } from '@mui/material'

type Props = {
  onSubmit: (employeeId: number, weekStart: string) => void
}

export default function TimesheetForm({ onSubmit }: Props) {
  const [employeeId, setEmployeeId] = useState('')
  const [weekStart, setWeekStart] = useState('')

  return (
    <Stack spacing={2} direction="row">
      <TextField label="Employee ID" value={employeeId} onChange={e => setEmployeeId(e.target.value)} />
      <TextField label="Week Start (YYYY-MM-DD)" value={weekStart} onChange={e => setWeekStart(e.target.value)} />
      <Button variant="outlined" onClick={() => onSubmit(Number(employeeId), weekStart)} disabled={!employeeId || !weekStart}>Create Timesheet</Button>
    </Stack>
  )
}

