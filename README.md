# Timesheet System

A simple timesheet system that captures employee hourly time on a weekly basis. Employees can manage their timesheet entries, and the API provides a basic reporting endpoint.

This repository contains:

- `api/` Spring Boot 3 REST API (H2 for dev)
- `ui/` React 18 + TypeScript (Vite)

Follow Agents.md and PROMPT.md for conventions and workflow.

## Prerequisites

- Java 17+
- Node.js 18+ and npm

## Backend (api/)

- Run API: `cd api && ./mvnw spring-boot:run` (or `mvn spring-boot:run`)
- Run tests: `cd api && ./mvnw test`
- Build JAR: `cd api && ./mvnw clean package`

Base URL: `/codex-example/api/v1`

Key endpoints:
- `POST /employees` create employee
- `POST /projects` create project
- `POST /timesheets` create timesheet for employee and week
- `POST /timesheets/{id}/entries` add entry (project/date/hours)
- `GET /reports/employee/{employeeId}?weekStart=YYYY-MM-DD` weekly summary

## Frontend (ui/)

- Install: `cd ui && npm install`
- Dev server: `cd ui && npm run dev` (http://localhost:5173)
  - Proxies `/codex-example` to `http://localhost:8080` during development
  - Run API and UI together for end-to-end flows
- Tests: `cd ui && npm test`
- Build: `cd ui && npm run build`

## Development Conventions

See `Agents.md` for architecture and coding standards. Controllers expose DTOs only; validation via Jakarta Bean Validation; MUI + hooks on frontend; colocated component files with tests.
