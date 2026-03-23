import { FormEvent, useState } from 'react'
import Alert from '@mui/material/Alert'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import Grid from '@mui/material/Unstable_Grid2'
import Paper from '@mui/material/Paper'
import Stack from '@mui/material/Stack'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import TextField from '@mui/material/TextField'
import Typography from '@mui/material/Typography'
import { useProjects } from '../../hooks/useProjects'

export const ProjectManager = () => {
  const { projects, addProject, error, loading } = useProjects()
  const [code, setCode] = useState('')
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const resetForm = () => {
    setCode('')
    setName('')
    setDescription('')
  }

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setSubmitting(true)
    try {
      await addProject({ code, name, description })
      resetForm()
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <Stack spacing={4}>
      <Paper sx={{ p: 3 }} elevation={1} component="form" onSubmit={handleSubmit}>
        <Typography variant="h6" gutterBottom>
          Add Project
        </Typography>
        <Grid container spacing={2}>
          <Grid xs={12} sm={6}>
            <TextField
              label="Project Code"
              value={code}
              onChange={(event) => setCode(event.target.value)}
              required
              fullWidth
            />
          </Grid>
          <Grid xs={12} sm={6}>
            <TextField
              label="Project Name"
              value={name}
              onChange={(event) => setName(event.target.value)}
              required
              fullWidth
            />
          </Grid>
          <Grid xs={12}>
            <TextField
              label="Description"
              value={description}
              onChange={(event) => setDescription(event.target.value)}
              multiline
              minRows={2}
              fullWidth
            />
          </Grid>
        </Grid>
        <Box mt={2}>
          <Button type="submit" variant="contained" disabled={submitting}>
            Save Project
          </Button>
        </Box>
        {error && (
          <Box mt={2}>
            <Alert severity="error">{error}</Alert>
          </Box>
        )}
      </Paper>

      <Paper sx={{ p: 3 }} elevation={1}>
        <Typography variant="h6" gutterBottom>
          Projects
        </Typography>
        {loading ? (
          <Typography>Loading projects...</Typography>
        ) : (
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Code</TableCell>
                <TableCell>Name</TableCell>
                <TableCell>Description</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {projects.map((project) => (
                <TableRow key={project.id}>
                  <TableCell>{project.code}</TableCell>
                  <TableCell>{project.name}</TableCell>
                  <TableCell>{project.description ?? '-'}</TableCell>
                </TableRow>
              ))}
              {projects.length === 0 && (
                <TableRow>
                  <TableCell colSpan={3}>No projects found.</TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        )}
      </Paper>
    </Stack>
  )
}
