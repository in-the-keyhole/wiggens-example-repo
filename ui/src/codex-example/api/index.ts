import axios from 'axios';

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/codex-example/api/v1',
});

export type Employee = { id: number; fullName: string; email: string };
export type TimesheetEntry = { id?: number; date: string; project?: string; hours: number };
export type TimesheetRequest = { employeeId: number; weekStart: string; entries: TimesheetEntry[] };
export type TimesheetResponse = { id: number; employeeId: number; employeeName: string; weekStart: string; entries: TimesheetEntry[]; totalHours: number };
export type SummaryRow = { employeeId: number; employeeName: string; fromDate: string; toDate: string; totalHours: number };
export type DashboardMetrics = { employeesCount: number; totalHoursThisWeek: string | number; totalHoursAllTime: string | number };

export const EmployeesApi = {
  list: async () => (await api.get<Employee[]>(`/employees`)).data,
  create: async (e: Omit<Employee, 'id'>) => (await api.post<Employee>(`/employees`, e)).data,
};

export const TimesheetsApi = {
  upsert: async (req: TimesheetRequest) => (await api.post<TimesheetResponse>(`/timesheets`, req)).data,
  getForWeek: async (employeeId: number, weekStart: string) => (await api.get<TimesheetResponse>(`/timesheets/${employeeId}`, { params: { weekStart } })).data,
};

export const ReportsApi = {
  summary: async (from: string, to: string) => (await api.get<SummaryRow[]>(`/reports/summary`, { params: { from, to } })).data,
};

export const DashboardApi = {
  metrics: async () => (await api.get<DashboardMetrics>(`/dashboard`)).data,
};
