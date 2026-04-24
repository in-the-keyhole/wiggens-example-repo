import React, { useEffect, useState } from 'react';
import { EmployeesApi, type Employee } from '../../codex-example/api';
import { Box, Button, Paper, Stack, TextField, Typography } from '@mui/material';
import { Link } from 'react-router-dom';

const App: React.FC = () => {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');

  const refresh = async () => setEmployees(await EmployeesApi.list());
  useEffect(() => { refresh(); }, []);

  const addEmployee = async () => {
    if (!fullName || !email) return;
    await EmployeesApi.create({ fullName, email });
    setFullName(''); setEmail('');
    refresh();
  };

  return (
    <Stack spacing={3}>
      <Typography variant="h5">Employees</Typography>
      <Paper sx={{ p: 2 }}>
        <Stack direction={{ xs:'column', sm:'row' }} spacing={2}>
          <TextField size="small" label="Full Name" value={fullName} onChange={e=>setFullName(e.target.value)} />
          <TextField size="small" label="Email" value={email} onChange={e=>setEmail(e.target.value)} />
          <Button variant="contained" onClick={addEmployee}>Add</Button>
        </Stack>
      </Paper>
      <Stack spacing={1}>
        {employees.map(e => (
          <Paper key={e.id} sx={{ p: 2 }}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <div>
                <Typography>{e.fullName}</Typography>
                <Typography variant="body2" color="text.secondary">{e.email}</Typography>
              </div>
              <Button component={Link} to={`/timesheet/${e.id}`} variant="outlined">Open Timesheet</Button>
            </Box>
          </Paper>
        ))}
      </Stack>
    </Stack>
  );
};

export default App;

