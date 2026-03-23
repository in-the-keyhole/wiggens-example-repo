import { FormEvent, useMemo, useState } from 'react'
import Alert from '@mui/material/Alert'
import Button from '@mui/material/Button'
import Card from '@mui/material/Card'
import CardContent from '@mui/material/CardContent'
import CardHeader from '@mui/material/CardHeader'
import Divider from '@mui/material/Divider'
import Grid from '@mui/material/Unstable_Grid2'
import IconButton from '@mui/material/IconButton'
import MenuItem from '@mui/material/MenuItem'
import Paper from '@mui/material/Paper'
import Stack from '@mui/material/Stack'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import TextField from '@mui/material/TextField'
import Tooltip from '@mui/material/Tooltip'
import Typography from '@mui/material/Typography'
import DeleteIcon from '@mui/icons-material/Delete'
import RestartAltIcon from '@mui/icons-material/RestartAlt'
import { useEmployees } from '../../hooks/useEmployees'
import { useProjects } from '../../hooks/useProjects'
import { useTimesheets } from '../../hooks/useTimesheets'
import type { TimesheetEntryPayload, TimesheetPayload } from '../../codex-example/api/types'

interface TimesheetFormEntry extends Omit<TimesheetEntryPayload, 'hours'> {
  hours: string
}

const toIsoDate = (date: Date): string => date.toISOString().split('T')[0]

const currentMonday = (): string => {
  const today = new Date()
  const day = today.getDay()
  const diff = (day + 6) % 7
  today.setDate(today.getDate() - diff)
  return toIsoDate(today)
}

const calculateWeekEnd = (weekStart: string): string => {
  const date = new Date(weekStart)
  date.setDate(date.getDate() + 6)
  return toIsoDate(date)
}

const createEmptyEntry = (weekStart: string): TimesheetFormEntry => ({
  projectId: '',
  workDate: weekStart,
  hours: '',
  notes: ''
})

export const TimesheetManager = () => {
  const { employees } = useEmployees()
  const { projects } = useProjects()
  const initialMonday = currentMonday()
  const [selectedEmployeeId, setSelectedEmployeeId] = useState('')
  const [weekStart, setWeekStart] = useState(initialMonday)
  const [entries, setEntries] = useState<TimesheetFormEntry[]>([createEmptyEntry(initialMonday)])
  const [selectedTimesheetId, setSelectedTimesheetId] = useState<string | null>(null)
  const [feedback, setFeedback] = useState<string | null>(null)
  const [errorMessage, setErrorMessage] = useState<string | null>(null)

  const { timesheets, loading, create, update } = useTimesheets(selectedEmployeeId)

  const totalHours = useMemo(
    () =>
      entries.reduce((total, entry) => {
        const hours = Number(entry.hours ?? 0)
        return Number.isNaN(hours) ? total : total + hours
      }, 0),
    [entries]
  )

  const handleEmployeeChange = (value: string) => {
    setSelectedEmployeeId(value)
    setSelectedTimesheetId(null)
    setEntries([createEmptyEntry(weekStart)])
  }

  const handleWeekStartChange = (value: string) => {
    setWeekStart(value)
    setEntries((prev) => prev.map((entry, index) => (index === 0 && !entry.projectId ? { ...entry, workDate: value } : entry)))
  }

  const handleEntryChange = (index: number, field: keyof TimesheetFormEntry, value: string) => {
    setEntries((prev) => prev.map((entry, entryIndex) => (entryIndex === index ? { ...entry, [field]: value } : entry)))
  }

  const addEntryRow = () => {
    setEntries((prev) => [...prev, createEmptyEntry(weekStart)])
  }

  const removeEntryRow = (index: number) => {
    if (entries.length === 1) {
      return
    }
    setEntries((prev) => prev.filter((_, entryIndex) => entryIndex !== index))
  }

  const resetForm = () => {
    setSelectedTimesheetId(null)
    const monday = currentMonday()
    setWeekStart(monday)
    setEntries([createEmptyEntry(monday)])
    setFeedback(null)
    setErrorMessage(null)
  }

  const populateForm = (timesheetIndex: number) => {
    const timesheet = timesheets[timesheetIndex]
    if (!timesheet) {
      return
    }
    setSelectedTimesheetId(timesheet.id)
    setWeekStart(timesheet.weekStart)
    setEntries(
      timesheet.entries.map((entry) => ({
        projectId: entry.projectId,
        workDate: entry.workDate,
        hours: entry.hours.toString(),
        notes: entry.notes ?? ''
      }))
    )
  }

  const validateEntries = (): string | null => {
    if (!selectedEmployeeId) {
      return 'Select an employee before saving.'
    }
    if (!weekStart) {
      return 'Select a week start date.'
    }
    if (entries.some((entry) => !entry.projectId || !entry.workDate || entry.hours.trim() === '')) {
      return 'Each entry must include a project, work date, and hours.'
    }
    if (entries.some((entry) => Number(entry.hours) <= 0)) {
      return 'Hours must be greater than zero.'
    }
    return null
  }

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setFeedback(null)
    const validationError = validateEntries()
    if (validationError) {
      setErrorMessage(validationError)
      return
    }
    setErrorMessage(null)

    const payload: TimesheetPayload = {
      employeeId: selectedEmployeeId,
      weekStart,
      entries: entries.map(({ projectId, workDate, hours, notes }) => ({
        projectId,
        workDate,
        hours: Number(hours),
        notes: notes?.trim() ? notes : undefined
      }))
    }

    try {
      if (selectedTimesheetId) {
        await update(selectedTimesheetId, payload)
        setFeedback('Timesheet updated successfully.')
      } else {
        const created = await create(payload)
        setSelectedTimesheetId(created.id)
        setFeedback('Timesheet created successfully.')
      }
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Failed to save timesheet')
    }
  }

  return (
    <Stack spacing={4}>
      <Paper component="form" onSubmit={handleSubmit} sx={{ p: 3 }} elevation={1}>
        <Stack spacing={3}>
          <Typography variant="h6">Timesheet Editor</Typography>
          <Grid container spacing={2}>
            <Grid xs={12} sm={6}>
              <TextField
                select
                label="Employee"
                value={selectedEmployeeId}
                onChange={(event) => handleEmployeeChange(event.target.value)}
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
            </Grid>
            <Grid xs={12} sm={6}>
              <TextField
                label="Week Start"
                type="date"
                value={weekStart}
                onChange={(event) => handleWeekStartChange(event.target.value)}
                required
                fullWidth
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
          </Grid>

          <Card variant="outlined">
            <CardHeader
              title="Entries"
              subheader={`Total hours: ${totalHours.toFixed(2)} (Week ends ${calculateWeekEnd(weekStart)})`}
              action={
                <Button variant="outlined" onClick={addEntryRow}>
                  Add Entry
                </Button>
              }
            />
            <Divider />
            <CardContent>
              <Stack spacing={2}>
                {entries.map((entry, index) => (
                  <Paper key={index} variant="outlined" sx={{ p: 2 }}>
                    <Grid container spacing={2} alignItems="center">
                      <Grid xs={12} md={4}>
                        <TextField
                          select
                          label="Project"
                          value={entry.projectId}
                          onChange={(event) => handleEntryChange(index, 'projectId', event.target.value)}
                          required
                          fullWidth
                        >
                          <MenuItem value="" disabled>
                            Select project
                          </MenuItem>
                          {projects.map((project) => (
                            <MenuItem key={project.id} value={project.id}>
                              {project.code} - {project.name}
                            </MenuItem>
                          ))}
                        </TextField>
                      </Grid>
                      <Grid xs={12} md={3}>
                        <TextField
                          label="Work Date"
                          type="date"
                          value={entry.workDate}
                          onChange={(event) => handleEntryChange(index, 'workDate', event.target.value)}
                          required
                          fullWidth
                          InputLabelProps={{ shrink: true }}
                        />
                      </Grid>
                      <Grid xs={12} md={2}>
                        <TextField
                          label="Hours"
                          type="number"
                          inputProps={{ step: 0.25, min: 0.25 }}
                          value={entry.hours}
                          onChange={(event) => handleEntryChange(index, 'hours', event.target.value)}
                          required
                          fullWidth
                        />
                      </Grid>
                      <Grid xs={11} md={2}>
                        <TextField
                          label="Notes"
                          value={entry.notes}
                          onChange={(event) => handleEntryChange(index, 'notes', event.target.value)}
                          fullWidth
                        />
                      </Grid>
                      <Grid
                        xs={12}
                        md={1}
                        display="flex"
                        justifyContent={{ xs: 'flex-start', md: 'flex-end' }}
                        alignItems="center"
                      >
                        <Tooltip title="Remove entry">
                          <span>
                            <IconButton
                              onClick={() => removeEntryRow(index)}
                              disabled={entries.length === 1}
                              color="error"
                              size="large"
                            >
                              <DeleteIcon />
                            </IconButton>
                          </span>
                        </Tooltip>
                      </Grid>
                    </Grid>
                  </Paper>
                ))}
              </Stack>
            </CardContent>
          </Card>

          <Stack direction="row" spacing={2}>
            <Button type="submit" variant="contained" disabled={!selectedEmployeeId}>
              {selectedTimesheetId ? 'Update Timesheet' : 'Create Timesheet'}
            </Button>
            <Button variant="outlined" startIcon={<RestartAltIcon />} onClick={resetForm}>
              New Timesheet
            </Button>
          </Stack>

          {feedback && <Alert severity="success">{feedback}</Alert>}
          {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
        </Stack>
      </Paper>

      <Paper sx={{ p: 3 }} elevation={1}>
        <Typography variant="h6" gutterBottom>
          Existing Timesheets
        </Typography>
        {!selectedEmployeeId ? (
          <Typography>Select an employee to view their timesheets.</Typography>
        ) : loading ? (
          <Typography>Loading timesheets...</Typography>
        ) : timesheets.length === 0 ? (
          <Typography>No timesheets found for this employee.</Typography>
        ) : (
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Week Start</TableCell>
                <TableCell>Status</TableCell>
                <TableCell align="right">Total Hours</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {timesheets.map((timesheet, index) => {
                const hours = timesheet.entries.reduce((total, entry) => total + Number(entry.hours ?? 0), 0)
                return (
                  <TableRow key={timesheet.id} hover selected={selectedTimesheetId === timesheet.id}>
                    <TableCell>{timesheet.weekStart}</TableCell>
                    <TableCell>{timesheet.status}</TableCell>
                    <TableCell align="right">{hours.toFixed(2)}</TableCell>
                    <TableCell align="right">
                      <Button size="small" onClick={() => populateForm(index)}>
                        Edit
                      </Button>
                    </TableCell>
                  </TableRow>
                )
              })}
            </TableBody>
          </Table>
        )}
      </Paper>
    </Stack>
  )
}
