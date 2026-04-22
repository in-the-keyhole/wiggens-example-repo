import { Box, Button, Grid, MenuItem, TextField } from '@mui/material'
import dayjs from 'dayjs'
import isoWeek from 'dayjs/plugin/isoWeek'
import { useState } from 'react'
import { TimesheetDTO } from '../../api/client'

dayjs.extend(isoWeek)

type Props = {
  onSubmit: (dto: TimesheetDTO) => Promise<void> | void
  initialEmployeeId?: number
}

export default function TimesheetForm({ onSubmit, initialEmployeeId }: Props) {
  const monday = dayjs().isoWeekday(1).format('YYYY-MM-DD')
  const [employeeId, setEmployeeId] = useState<number>(initialEmployeeId ?? 1)
  const [weekStart, setWeekStart] = useState<string>(monday)
  const [hours, setHours] = useState<Record<string, number>>({
    mon: 8, tue: 8, wed: 8, thu: 8, fri: 8, sat: 0, sun: 0
  })

  const handle = (k: string) => (e: React.ChangeEvent<HTMLInputElement>) => setHours({ ...hours, [k]: Number(e.target.value) })

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    const dto: TimesheetDTO = {
      employeeId,
      weekStart,
      hoursMon: hours.mon,
      hoursTue: hours.tue,
      hoursWed: hours.wed,
      hoursThu: hours.thu,
      hoursFri: hours.fri,
      hoursSat: hours.sat,
      hoursSun: hours.sun
    }
    await onSubmit(dto)
  }

  return (
    <Box component="form" onSubmit={submit}>
      <Grid container spacing={2}>
        <Grid item xs={12} md={4}>
          <TextField fullWidth label="Employee ID" type="number" value={employeeId} onChange={e => setEmployeeId(Number(e.target.value))} />
        </Grid>
        <Grid item xs={12} md={4}>
          <TextField fullWidth label="Week Start (Mon)" type="date" value={weekStart} onChange={e => setWeekStart(e.target.value)} />
        </Grid>
        {[
          ['Mon','mon'],['Tue','tue'],['Wed','wed'],['Thu','thu'],['Fri','fri'],['Sat','sat'],['Sun','sun']
        ].map(([label,key]) => (
          <Grid item xs={6} md={2} key={key}>
            <TextField fullWidth label={`${label} Hours`} type="number" inputProps={{ min:0, max:24, step:0.5 }} value={hours[key as keyof typeof hours]} onChange={handle(key)} />
          </Grid>
        ))}
        <Grid item xs={12}>
          <Button type="submit" variant="contained">Save</Button>
        </Grid>
      </Grid>
    </Box>
  )
}
