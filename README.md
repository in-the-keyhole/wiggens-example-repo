# Wiggens Timesheet System

This repository contains a full‑stack timesheet application with a Spring Boot backend and a React (Vite + TypeScript) frontend.

The implementation follows the conventions in Agents.md and addresses GitHub issues prefixed with "wiggens:".

## Project Structure

```
api/       # Spring Boot 3 backend (Maven)
ui/        # React frontend (Vite + TypeScript + MUI)
Agents.md  # Project instructions
PROMPT.md  # Loop prompt and workflow rules
```

## Prerequisites

- Java 21+
- Maven 3.8+
- Node 18+ and npm 10+

## Backend (api/)

Run the API on port 8080 using an in‑memory H2 database for development.

- Run: `cd api && mvn spring-boot:run`
- Test: `cd api && mvn test`
- Build: `cd api && mvn clean package`

H2 console is available at `/h2-console` while running. Default JDBC URL: `jdbc:h2:mem:timesheetdb`.

Base REST path: `/codex-example/api/v1/`

Key endpoints:
- `GET /codex-example/api/v1/employees`
- `POST /codex-example/api/v1/employees`
- `GET /codex-example/api/v1/timesheets`
- `POST /codex-example/api/v1/timesheets`
- `GET /codex-example/api/v1/reports/summary`

## Frontend (ui/)

Run the React dev server on port 5173 and connect to the backend at `http://localhost:8080`.

- Install: `cd ui && npm install`
- Dev: `cd ui && npm run dev`
- Test: `cd ui && npm test`
- Build: `cd ui && npm run build`

The UI provides a landing Dashboard showing employee count, total hours for the current week, and total hours to date. It also includes navigation to browse timesheets and create a new timesheet for a selected employee (Material UI themed).

## GitHub Workflow

- Work one issue at a time on issues titled with the `wiggens:` prefix.
- Use conventional commits (`feat:`, `fix:`, `chore:`, `test:`).
- Run tests before marking an issue complete.
- Push to `origin/main`.

## Configuration

See `api/src/main/resources/application.properties` for dev database configuration. The frontend API base URL is configured in `ui/src/codex-example/api/client.ts`.

