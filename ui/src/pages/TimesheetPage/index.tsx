import React, { useEffect, useMemo, useState } from 'react';
import { useParams } from 'react-router-dom';
import { TimesheetsApi, type TimesheetEntry, type TimesheetRequest } from '../../codex-example/api';
import { Stack, Typography, TextField, Paper, Button } from '@mui/material';

function startOfWeek(d: Date) {
  const date = new Date(d);
  const day = date.getDay();
  const diff = (day === 0 ? -6 : 1) - day; // Monday start
  date.setDate(date.getDate() + diff);
  date.setHours(0,0,0,0);
  return date;
}

const TimesheetPage: React.FC = () => {
  const { employeeId } = useParams();
  const [weekStart, setWeekStart] = useState(() => startOfWeek(new Date()));
  const [entries, setEntries] = useState<TimesheetEntry[]>([]);
  const isoWeek = useMemo(() => weekStart.toISOString().slice(0,10), [weekStart]);

  useEffect(() => {
    const load = async () => {
      try {
        const resp = await TimesheetsApi.getForWeek(Number(employeeId), isoWeek);
        setEntries(resp.entries);
      } catch {
        // no existing timesheet; initialize 7 days
        const init: TimesheetEntry[] = Array.from({ length: 7 }).map((_, i) => ({
          date: new Date(weekStart.getTime() + i*86400000).toISOString().slice(0,10),
          project: '',
          hours: 0,
        }));
        setEntries(init);
      }
    };
    load();
  }, [employeeId, isoWeek]);

  const save = async () => {
    const payload: TimesheetRequest = {
      employeeId: Number(employeeId),
      weekStart: isoWeek,
      entries: entries.map(e => ({ date: e.date, project: e.project, hours: Number(e.hours) }))
    };
    await TimesheetsApi.upsert(payload);
  };

  const changeHours = (idx: number, value: string) => {
    const updated = [...entries];
    updated[idx] = { ...updated[idx], hours: Number(value) };
    setEntries(updated);
  };
  const changeProject = (idx: number, value: string) => {
    const updated = [...entries];
    updated[idx] = { ...updated[idx], project: value };
    setEntries(updated);
  };

  return (
    <Stack spacing={2}>
      <Typography variant="h6">Timesheet</Typography>
      <Stack direction={{ xs:'column', sm:'row' }} spacing={2}>
        <TextField
          type="date"
          label="Week Start"
          InputLabelProps={{ shrink: true }}
          value={isoWeek}
          onChange={e => setWeekStart(startOfWeek(new Date(e.target.value)))}
        />
        <Button variant="contained" onClick={save}>Save</Button>
      </Stack>

      <Stack spacing={1}>
        {entries.map((e, i) => (
          <Paper key={i} sx={{ p: 2, display: 'flex', gap: 2 }}>
            <TextField label="Date" value={e.date} InputLabelProps={{ shrink: true }} size="small" disabled />
            <TextField label="Project" value={e.project || ''} size="small" onChange={ev => changeProject(i, ev.target.value)} />
            <TextField label="Hours" type="number" size="small" value={e.hours} onChange={ev => changeHours(i, ev.target.value)} />
          </Paper>
        ))}
      </Stack>
    </Stack>
  );
};

export default TimesheetPage;

