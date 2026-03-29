import { Alert, Button, Stack, TextField, Typography } from '@mui/material'
import { useEffect, useState } from 'react'
import { createEmployee, createProject, listEmployees, listProjects } from '../../codex-example/api/client'

export default function Landing() {
  const [first, setFirst] = useState('')
  const [last, setLast] = useState('')
  const [project, setProject] = useState('')
  const [employees, setEmployees] = useState<any[]>([])
  const [projects, setProjects] = useState<any[]>([])
  const [msg, setMsg] = useState<string | null>(null)

  const refresh = async () => {
    setEmployees(await listEmployees())
    setProjects(await listProjects())
  }

  useEffect(() => { void refresh() }, [])

  const addEmp = async () => {
    if (!first || !last) return
    await createEmployee(first, last)
    setFirst(''); setLast(''); setMsg('Employee created')
    await refresh()
  }
  const addProj = async () => {
    if (!project) return
    await createProject(project)
    setProject(''); setMsg('Project created')
    await refresh()
  }

  return (
    <Stack spacing={2}>
      <Typography variant="h5">Welcome to Timesheet</Typography>
      {msg && <Alert onClose={() => setMsg(null)}>{msg}</Alert>}
      <Stack direction="row" spacing={1}>
        <TextField size="small" label="First Name" value={first} onChange={e => setFirst(e.target.value)} />
        <TextField size="small" label="Last Name" value={last} onChange={e => setLast(e.target.value)} />
        <Button variant="contained" onClick={addEmp}>Add Employee</Button>
      </Stack>
      <Stack direction="row" spacing={1}>
        <TextField size="small" label="Project Name" value={project} onChange={e => setProject(e.target.value)} />
        <Button variant="contained" onClick={addProj}>Add Project</Button>
      </Stack>
      <Stack direction="row" spacing={4}>
        <Stack>
          <Typography variant="subtitle1">Employees</Typography>
          {employees.map(e => <Typography key={e.id}>{e.firstName} {e.lastName}</Typography>)}
        </Stack>
        <Stack>
          <Typography variant="subtitle1">Projects</Typography>
          {projects.map(p => <Typography key={p.id}>{p.name}</Typography>)}
        </Stack>
      </Stack>
    </Stack>
  )
}

