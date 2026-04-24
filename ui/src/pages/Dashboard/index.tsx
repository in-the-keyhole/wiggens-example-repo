import React, { useEffect, useState } from 'react';
import { DashboardApi, EmployeesApi, type DashboardMetrics, type Employee } from '../../codex-example/api';
import { Box, Button, Card, CardContent, FormControl, InputLabel, MenuItem, Select, Stack, Typography } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';

const StatCard: React.FC<{ label: string; value: React.ReactNode }> = ({ label, value }) => (
  <Card sx={{ flex: 1, minWidth: 240 }}>
    <CardContent>
      <Typography color="text.secondary" gutterBottom>{label}</Typography>
      <Typography variant="h4">{value}</Typography>
    </CardContent>
  </Card>
);

const Dashboard: React.FC = () => {
  const [metrics, setMetrics] = useState<DashboardMetrics | null>(null);
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [selected, setSelected] = useState<number | ''>('');
  const navigate = useNavigate();

  useEffect(() => {
    const load = async () => {
      setMetrics(await DashboardApi.metrics());
      setEmployees(await EmployeesApi.list());
    };
    load();
  }, []);

  const goToTimesheet = () => {
    if (selected) navigate(`/timesheet/${selected}`);
  };

  return (
    <Stack spacing={3}>
      <Typography variant="h5">Dashboard</Typography>

      <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
        <StatCard label="Employees" value={metrics?.employeesCount ?? '—'} />
        <StatCard label="Total Hours (This Week)" value={String(metrics?.totalHoursThisWeek ?? '—')} />
        <StatCard label="Total Hours (All Time)" value={String(metrics?.totalHoursAllTime ?? '—')} />
      </Stack>

      <Card>
        <CardContent>
          <Typography variant="h6" gutterBottom>Quick Actions</Typography>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} alignItems={{ xs: 'stretch', sm: 'center' }}>
            <Button variant="outlined" component={Link} to="/employees">Browse Employees & Timesheets</Button>

            <Box sx={{ flexGrow: 1 }} />
            <FormControl size="small" sx={{ minWidth: 220 }}>
              <InputLabel id="employee-select-label">Select Employee</InputLabel>
              <Select
                labelId="employee-select-label"
                label="Select Employee"
                value={selected}
                onChange={(e) => setSelected(e.target.value as number)}
              >
                {employees.map(e => (
                  <MenuItem key={e.id} value={e.id}>{e.fullName}</MenuItem>
                ))}
              </Select>
            </FormControl>
            <Button variant="contained" onClick={goToTimesheet} disabled={!selected}>Create/Open Timesheet</Button>
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  );
};

export default Dashboard;

