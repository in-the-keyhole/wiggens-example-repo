import { useEffect, useState } from 'react'
import { createTimesheet, getTimesheetsForWeek } from '../../shared/codex-example/api/timesheets'

type Entry = { projectCode: string; task: string; hours: number }

export default function TimesheetEditorPage() {
  const [employeeId, setEmployeeId] = useState('emp-001')
  const [weekStart, setWeekStart] = useState(() => new Date().toISOString().slice(0, 10))
  const [entries, setEntries] = useState<Entry[]>([
    { projectCode: 'PRJ-1', task: 'Development', hours: 0 },
    { projectCode: 'PRJ-2', task: 'Meetings', hours: 0 },
  ])
  const [status, setStatus] = useState<string>('')

  useEffect(() => {
    (async () => {
      try {
        const res = await getTimesheetsForWeek(employeeId, weekStart)
        if (res.length > 0) {
          setEntries(res[0].entries)
        }
      } catch (e) {
        // ignore if API not running
      }
    })()
  }, [employeeId, weekStart])

  const total = entries.reduce((sum, e) => sum + (Number(e.hours) || 0), 0)

  const updateEntry = (idx: number, patch: Partial<Entry>) => {
    setEntries(prev => prev.map((e, i) => i === idx ? { ...e, ...patch } : e))
  }

  const onSave = async () => {
    setStatus('Saving...')
    try {
      await createTimesheet({ employeeId, weekStart, entries })
      setStatus('Saved!')
    } catch (e) {
      setStatus('Failed to save (is API running on :8080?)')
    }
  }

  return (
    <div>
      <h2>Weekly Timesheet</h2>
      <div style={{ display: 'flex', gap: 12, alignItems: 'center', marginBottom: 12 }}>
        <label>
          Employee ID
          <input value={employeeId} onChange={e => setEmployeeId(e.target.value)} style={{ marginLeft: 8 }}/>
        </label>
        <label>
          Week Start
          <input type="date" value={weekStart} onChange={e => setWeekStart(e.target.value)} style={{ marginLeft: 8 }}/>
        </label>
      </div>
      <table style={{ borderCollapse: 'collapse', width: '100%', maxWidth: 700 }}>
        <thead>
          <tr>
            <th style={{ textAlign: 'left' }}>Project</th>
            <th style={{ textAlign: 'left' }}>Task</th>
            <th style={{ textAlign: 'right' }}>Hours</th>
          </tr>
        </thead>
        <tbody>
          {entries.map((e, idx) => (
            <tr key={idx}>
              <td><input value={e.projectCode} onChange={ev => updateEntry(idx, { projectCode: ev.target.value })} /></td>
              <td><input value={e.task} onChange={ev => updateEntry(idx, { task: ev.target.value })} /></td>
              <td style={{ textAlign: 'right' }}>
                <input type="number" min={0} max={24} value={e.hours}
                       onChange={ev => updateEntry(idx, { hours: Number(ev.target.value) })} />
              </td>
            </tr>
          ))}
        </tbody>
        <tfoot>
          <tr>
            <td colSpan={2} style={{ textAlign: 'right', fontWeight: 600 }}>Total</td>
            <td style={{ textAlign: 'right', fontWeight: 600 }}>{total}</td>
          </tr>
        </tfoot>
      </table>
      <div style={{ marginTop: 12, display: 'flex', gap: 8 }}>
        <button onClick={() => setEntries(prev => [...prev, { projectCode: '', task: '', hours: 0 }])}>Add Row</button>
        <button onClick={onSave}>Save</button>
        <span>{status}</span>
      </div>
    </div>
  )
}

