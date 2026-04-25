# Ralph Timesheet

Full-stack timesheet application.

## Stack
- Backend: Spring Boot 3, Spring Data JPA, H2 (dev) / PostgreSQL (prod), Maven
- Frontend: React (Vite + TypeScript) + MUI
- Testing: JUnit 5 + Spring Boot Test (backend), Vitest + React Testing Library (frontend)

## Structure
```
api/  # Spring Boot backend (port 8080)
ui/   # React frontend (port 5173)
```

## Backend
- Run: `cd api && mvn spring-boot:run`
- Test: `cd api && mvn test`
- Build: `cd api && mvn clean package`
- Base URL: `http://localhost:8080/codex-example/api/v1`
- Seeds example employee on startup: `John Doe <john.doe@example.com>`

## Frontend
- Install: `cd ui && npm install`
- Dev: `cd ui && npm run dev`
- Test: `cd ui && npm test`
- Build: `cd ui && npm run build`

## GitHub Workflow
- Use conventional commits: `feat:`, `fix:`, `chore:`, `test:`, `docs:`
- Push: `git push origin main`
- Issues with prefix `wiggens:` drive iterations; close via commit messages (e.g., `closes #11`).

## Frontend Dev Proxy
- During `npm run dev`, API calls to `/codex-example/api` are proxied to `http://localhost:8080`.

## Notes
- REST endpoints under `codex-example/api/v1/`
- Uses DTOs and Jakarta Validation
- H2 for local development
