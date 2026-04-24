import React, { useMemo, useState } from 'react';
import { ReportsApi, type SummaryRow } from '../../codex-example/api';
import { Button, Paper, Stack, TextField, Typography } from '@mui/material';

function startOfWeek(d: Date) {
  const date = new Date(d);
  const day = date.getDay();
  const diff = (day === 0 ? -6 : 1) - day; // Monday start
  date.setDate(date.getDate() + diff);
  date.setHours(0,0,0,0);
  return date;
}

const ReportPage: React.FC = () => {
  const [from, setFrom] = useState(() => startOfWeek(new Date()));
  const [to, setTo] = useState(() => new Date(startOfWeek(new Date()).getTime() + 6*86400000));
  const [rows, setRows] = useState<SummaryRow[]>([]);
  const isoFrom = useMemo(() => from.toISOString().slice(0,10), [from]);
  const isoTo = useMemo(() => to.toISOString().slice(0,10), [to]);

  const run = async () => setRows(await ReportsApi.summary(isoFrom, isoTo));

  return (
    <Stack spacing={2}>
      <Typography variant="h6">Summary Report</Typography>
      <Stack direction={{ xs:'column', sm:'row' }} spacing={2}>
        <TextField type="date" label="From" InputLabelProps={{ shrink: true }} value={isoFrom} onChange={e=>setFrom(new Date(e.target.value))} />
        <TextField type="date" label="To" InputLabelProps={{ shrink: true }} value={isoTo} onChange={e=>setTo(new Date(e.target.value))} />
        <Button variant="contained" onClick={run}>Run</Button>
      </Stack>
      <Stack spacing={1}>
        {rows.map((r, i) => (
          <Paper key={i} sx={{ p: 2, display: 'flex', gap: 2, justifyContent: 'space-between' }}>
            <Typography>{r.employeeName}</Typography>
            <Typography>{r.totalHours} hours</Typography>
          </Paper>
        ))}
        {rows.length === 0 && <Typography color="text.secondary">No results</Typography>}
      </Stack>
    </Stack>
  );
};

export default ReportPage;

