export interface Employee {
  id: string
  firstName: string
  lastName: string
  email: string
  active: boolean
}

export interface EmployeePayload {
  firstName: string
  lastName: string
  email: string
  active: boolean
}

export interface Project {
  id: string
  code: string
  name: string
  description?: string | null
}

export interface ProjectPayload {
  code: string
  name: string
  description?: string
}

export interface TimesheetEntryPayload {
  projectId: string
  workDate: string
  hours: number
  notes?: string
}

export interface TimesheetPayload {
  employeeId: string
  weekStart: string
  entries: TimesheetEntryPayload[]
}

export interface TimesheetEntry {
  id: string
  projectId: string
  projectCode: string
  projectName: string
  workDate: string
  hours: number
  notes?: string | null
}

export interface Timesheet {
  id: string
  employeeId: string
  employeeName: string
  weekStart: string
  status: string
  entries: TimesheetEntry[]
}

export interface EmployeeHoursReport {
  employeeId: string
  employeeName: string
  startDate: string
  endDate: string
  totalHours: string
  projectHours: Array<{
    projectId: string
    projectCode: string
    projectName: string
    hours: string
  }>
}
