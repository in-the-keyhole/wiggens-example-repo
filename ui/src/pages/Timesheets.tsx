import { useEffect, useState } from 'react'
import { TimesheetsApi, Timesheet } from '../codex-example/api/client'
import { DataGrid, GridColDef } from '@mui/x-data-grid'
import { Box, Typography } from '@mui/material'

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 90 },
  { field: 'employeeId', headerName: 'Employee', width: 140 },
  { field: 'weekStart', headerName: 'Week Start', width: 160 },
  { field: 'total', headerName: 'Total Hours', width: 140, valueGetter: (p) => (p.row.entries || []).reduce((s: number, e: any) => s + e.hours, 0) }
]

export default function Timesheets() {
  const [rows, setRows] = useState<Timesheet[]>([])
  useEffect(() => { (async () => setRows(await TimesheetsApi.list()))() }, [])
  return (
    <Box>
      <Typography variant="h5" gutterBottom>Timesheets</Typography>
      <div style={{ height: 450, width: '100%' }}>
        <DataGrid rows={rows} columns={columns} getRowId={(r) => r.id!} pageSizeOptions={[5, 10]} />
      </div>
    </Box>
  )
}

