Ralph Timesheet

Overview
- Full‑stack timesheet app per Agents.md.
- Backend: Spring Boot 3, Spring Data JPA, H2 (dev) / PostgreSQL (prod), Maven.
- Frontend: React + Vite + TypeScript + MUI.
- Tests: JUnit 5 + Spring Boot Test (API), Vitest + React Testing Library (UI).

Project Structure
- api/ — Spring Boot backend (Maven)
- ui/ — React frontend (Vite + TypeScript)
- Agents.md — project conventions and architecture
- PROMPT.md — loop/process instructions

Prerequisites
- Java 21, Maven Wrapper (`./mvnw`), Node 18+ and npm.

Backend (api/)
- Run API: `cd api && ./mvnw spring-boot:run` (port 8080)
- Run tests: `cd api && ./mvnw test`
- Build JAR: `cd api && ./mvnw clean package`

Backend Notes
- Base path: `/codex-example/api/v1`
- Key endpoints:
  - `POST /employees` — create employee
  - `GET /employees` — list employees
  - `GET /employees/{id}` — get employee
  - `PUT /employees/{id}` — update employee
  - `DELETE /employees/{id}` — delete employee
  - `POST /timesheets` — create/update weekly timesheet (upsert)
  - `GET /timesheets/employee/{employeeId}` — list timesheets for employee
  - `GET /timesheets/employee/{employeeId}/week?start=YYYY-MM-DD` — fetch week
- Dev DB: in‑memory H2; console at `/h2-console`.

Frontend (ui/)
- Install deps: `cd ui && npm install`
- Dev server: `cd ui && npm run dev` (port 5173)
- Run tests: `cd ui && npm test`
- Production build: `cd ui && npm run build`

Frontend Notes
- Routes: `/` (Landing), `/timesheet` (enter weekly hours), `/report` (summary by employee ID)
- API client base URL: `/codex-example/api/v1`
- Uses MUI theme with `ThemeProvider` and `CssBaseline`.

Development Conventions
- Backend: Controller -> Service -> Repository, DTOs only, Bean Validation, Lombok.
- Frontend: Functional components, hooks, colocated tests, shared API module, MUI.

Running End‑to‑End (dev)
1) Start API: `cd api && ./mvnw spring-boot:run`
2) Start UI: `cd ui && npm install && npm run dev`
3) Open `http://localhost:5173`

Testing
- Backend: `cd api && ./mvnw test`
- Frontend: `cd ui && npm test` (Vitest configured with jsdom + jest‑dom)

GitHub Workflow
- Use issues prefixed with `wiggens:`. Implement one issue per commit.
- Conventional commits: `feat:`, `fix:`, `chore:`, `test:`.
- Push to origin using configured PAT in `GITHUB_TOKEN` when closing issues.

