# Ralph Timesheet

A simple employee timesheet system with a Spring Boot backend and a React (Vite + TypeScript + MUI) frontend.

## Project Structure
- `api/` Spring Boot 3 + Spring Data JPA + H2 (dev)
- `ui/` React + Vite + TypeScript + MUI

## Prerequisites
- Java 17+
- Node.js 18+

## Backend (api/)
- Run: `cd api && ./mvnw spring-boot:run` (serves on `http://localhost:8080`)
- Test: `cd api && ./mvnw test`
- Build: `cd api && ./mvnw clean package`

REST base path: `http://localhost:8080/codex-example/api/v1`

Key endpoints
- `POST /employees` create employee
- `GET /employees` list employees
- `POST /timesheets` upsert a weekly timesheet
- `GET /timesheets/{employeeId}?weekStart=YYYY-MM-DD` get a weekly timesheet
- `GET /reports/summary?from=YYYY-MM-DD&to=YYYY-MM-DD` summary of hours per employee

## Frontend (ui/)
- Install: `cd ui && npm install`
- Dev: `cd ui && npm run dev` (serves on `http://localhost:5173`)
- Test: `cd ui && npm test`
- Build: `cd ui && npm run build`

The frontend expects the backend at `VITE_API_BASE_URL` (default `http://localhost:8080/codex-example/api/v1`).

## Development Conventions
- Backend: layered Controller -> Service -> Repository, DTOs, Jakarta Validation, tests with Spring Boot Test, Lombok.
- Frontend: functional components, hooks, MUI, colocated tests, shared API module at `ui/src/codex-example/api/` using Axios.

## Git
- Conventional commits (`feat:`, `fix:`, `chore:`, `test:`)
- One issue per commit; commit then push
