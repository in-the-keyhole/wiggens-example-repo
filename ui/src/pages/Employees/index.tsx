import { useMemo, useState } from 'react'
import { Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, Paper, Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography } from '@mui/material'
import EditIcon from '@mui/icons-material/Edit'
import DeleteIcon from '@mui/icons-material/Delete'
import useEmployees from '../../hooks/useEmployees'
import type { EmployeeDTO } from '../../api/client'

type FormState = { id?: number; name: string; email: string }

export default function EmployeesPage() {
  const { employees, loading, error, add, update, remove } = useEmployees()
  const [open, setOpen] = useState(false)
  const [form, setForm] = useState<FormState>({ name: '', email: '' })
  const [saving, setSaving] = useState(false)
  const isEdit = useMemo(() => typeof form.id === 'number', [form.id])

  const handleOpenCreate = () => { setForm({ name: '', email: '' }); setOpen(true) }
  const handleOpenEdit = (e: EmployeeDTO) => { setForm({ id: e.id, name: e.name, email: e.email }); setOpen(true) }
  const handleClose = () => { if (!saving) setOpen(false) }

  const handleDelete = async (e: EmployeeDTO) => {
    // simple confirm
    if (confirm(`Delete ${e.name}?`)) {
      await remove(e.id!)
    }
  }

  const handleSubmit = async () => {
    if (!form.name.trim() || !form.email.trim()) return
    setSaving(true)
    try {
      if (isEdit && form.id) {
        await update(form.id, { name: form.name.trim(), email: form.email.trim() })
      } else {
        await add({ name: form.name.trim(), email: form.email.trim() })
      }
      setOpen(false)
    } finally {
      setSaving(false)
    }
  }

  return (
    <Box>
      <Stack direction="row" alignItems="center" justifyContent="space-between" sx={{ mb: 2 }}>
        <Typography variant="h5">Employees</Typography>
        <Button variant="contained" onClick={handleOpenCreate}>Add Employee</Button>
      </Stack>

      {error && <Typography color="error" sx={{ mb: 1 }}>{error}</Typography>}

      <TableContainer component={Paper}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Email</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow><TableCell colSpan={3}>Loading…</TableCell></TableRow>
            ) : employees.length === 0 ? (
              <TableRow><TableCell colSpan={3}>No employees</TableCell></TableRow>
            ) : employees.map(e => (
              <TableRow key={e.id} data-testid={`row-${e.id}`}>
                <TableCell>{e.name}</TableCell>
                <TableCell>{e.email}</TableCell>
                <TableCell align="right">
                  <IconButton aria-label={`edit-${e.id}`} onClick={() => handleOpenEdit(e)} size="small"><EditIcon fontSize="small" /></IconButton>
                  <IconButton aria-label={`delete-${e.id}`} onClick={() => handleDelete(e)} size="small" color="error"><DeleteIcon fontSize="small" /></IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle>{isEdit ? 'Edit Employee' : 'Add Employee'}</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ mt: 1 }}>
            <TextField
              autoFocus
              label="Name"
              value={form.name}
              onChange={e => setForm(f => ({ ...f, name: e.target.value }))}
            />
            <TextField
              label="Email"
              type="email"
              value={form.email}
              onChange={e => setForm(f => ({ ...f, email: e.target.value }))}
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} disabled={saving}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained" disabled={saving || !form.name.trim() || !form.email.trim()}>
            {isEdit ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  )
}

