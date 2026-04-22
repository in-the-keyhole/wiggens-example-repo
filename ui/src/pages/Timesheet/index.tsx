import { Alert, Paper, Snackbar, Typography } from '@mui/material'
import TimesheetForm from '../../components/TimesheetForm'
import { useTimesheet } from '../../hooks/useTimesheet'

export default function TimesheetPage() {
  const { upsert, loading, error } = useTimesheet()
  const [open, setOpen] = useState(false)
  async function handleSubmit(dto: any) {
    await upsert(dto)
    setOpen(true)
  }
  return (
    <Paper sx={{ p: 2 }}>
      <Typography variant="h5" gutterBottom>Enter Weekly Timesheet</Typography>
      <TimesheetForm onSubmit={handleSubmit} />
      <Snackbar open={open} autoHideDuration={3000} onClose={() => setOpen(false)}>
        <Alert severity="success">Timesheet saved</Alert>
      </Snackbar>
      {error && <Alert severity="error">{error}</Alert>}
    </Paper>
  )
}

