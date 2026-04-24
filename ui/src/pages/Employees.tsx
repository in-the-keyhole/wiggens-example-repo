import { Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Stack, TextField } from '@mui/material'
import { DataGrid, GridColDef } from '@mui/x-data-grid'
import { useEffect, useMemo, useState } from 'react'
import { createEmployee, deleteEmployee, Employee, listEmployees, updateEmployee } from '../api/client'

export default function Employees() {
  const [rows, setRows] = useState<Employee[]>([])
  const [open, setOpen] = useState(false)
  const [editing, setEditing] = useState<Employee | null>(null)

  const load = () => listEmployees().then(setRows)
  useEffect(() => { load() }, [])

  const cols: GridColDef[] = useMemo(() => [
    { field: 'id', headerName: 'ID', width: 80 },
    { field: 'firstName', headerName: 'First Name', flex: 1 },
    { field: 'lastName', headerName: 'Last Name', flex: 1 },
    { field: 'email', headerName: 'Email', flex: 1.5 },
  ], [])

  const onSave = async () => {
    if (!editing) return
    if (editing.id) await updateEmployee(editing.id, editing)
    else await createEmployee(editing)
    setOpen(false); setEditing(null); load()
  }

  const onDelete = async () => {
    if (editing?.id) { await deleteEmployee(editing.id); setOpen(false); setEditing(null); load() }
  }

  return (
    <Box>
      <Stack direction="row" justifyContent="flex-end" sx={{ mb: 1 }}>
        <Button variant="contained" onClick={() => { setEditing({ firstName: '', lastName: '', email: '' }); setOpen(true) }}>Add Employee</Button>
      </Stack>
      <div style={{ height: 420, width: '100%' }}>
        <DataGrid rows={rows} columns={cols} onRowDoubleClick={(p) => { setEditing(p.row as Employee); setOpen(true) }} />
      </div>

      <Dialog open={open} onClose={() => setOpen(false)}>
        <DialogTitle>{editing?.id ? 'Edit Employee' : 'New Employee'}</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ mt: 1 }}>
            <TextField label="First Name" value={editing?.firstName ?? ''} onChange={e => setEditing({ ...editing!, firstName: e.target.value })} />
            <TextField label="Last Name" value={editing?.lastName ?? ''} onChange={e => setEditing({ ...editing!, lastName: e.target.value })} />
            <TextField label="Email" value={editing?.email ?? ''} onChange={e => setEditing({ ...editing!, email: e.target.value })} />
          </Stack>
        </DialogContent>
        <DialogActions>
          {editing?.id && <Button color="error" onClick={onDelete}>Delete</Button>}
          <Button onClick={() => setOpen(false)}>Cancel</Button>
          <Button variant="contained" onClick={onSave}>Save</Button>
        </DialogActions>
      </Dialog>
    </Box>
  )
}

