# Wiggens Example Repo — Timesheet System

This repository contains a minimal full‑stack Timesheet System per the open GitHub issue. It includes:

- Backend: Spring Boot 3 (REST API), Spring Data JPA, H2 for dev
- Frontend: React 18 + TypeScript (Vite), React Router, Axios, MUI

## Project Structure

```
api/          # Spring Boot backend (Maven project)
ui/           # React frontend (Vite + TypeScript)
Agents.md     # Architecture & conventions
PROMPT.md     # Implementation guidelines
```

## Getting Started

### Backend (api)

Prereqs: Java 17+ (tested on 21), Maven 3.8+

Commands:

```
cd api
mvn spring-boot:run            # Run API on http://localhost:8080
mvn test                       # Run backend tests
mvn clean package              # Build JAR
```

The dev profile uses an in‑memory H2 database with web console at `/h2-console` (JDBC URL `jdbc:h2:mem:timesheet;MODE=PostgreSQL`).

### Frontend (ui)

Prereqs: Node 18+, npm 9+

Commands:

```
cd ui
npm install
npm run dev                    # Dev server on http://localhost:5173
npm test                       # Run frontend tests
npm run build                  # Production build
```

## API Overview

Base path: `/codex-example/api/v1`

- `POST /employees` — create employee
- `GET /employees` — list employees
- `POST /projects` — create project
- `GET /projects` — list projects
- `POST /timesheets` — upsert weekly timesheet (entries for the week)
- `GET /timesheets?employeeId=..&weekStart=YYYY-MM-DD` — fetch weekly timesheet
- `GET /dashboard/weekly?weekStart=YYYY-MM-DD` — weekly summary per employee

All requests/responses use DTOs and validate inputs via Jakarta Bean Validation.

## Development Conventions

Follow `Agents.md`:

- Layered architecture: Controller → Service → Repository
- DTOs only over the wire (no entities exposed)
- Validation annotations on inputs
- Integration tests for controllers/services
- React functional components with hooks and colocated tests
- Shared Axios client under `src/codex-example/api/`

## Notes

- This is a minimal, working slice to satisfy the open issue. It is structured to be extended (e.g., auth, roles, real PostgreSQL, richer dashboard KPIs). Further refinements and test coverage can be added iteratively.
