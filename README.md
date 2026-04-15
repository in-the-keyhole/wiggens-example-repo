# Ralph Timesheet

Monorepo for a simple employee timesheet system.

## Structure
- `api/` Spring Boot 3 backend (REST under `/codex-example/api/v1`)
- `ui/` React + Vite + TypeScript frontend (MUI)
- `Agents.md` Architecture and conventions
- `PROMPT.md` Iteration rules

## Prerequisites
- Java 17+
- Node.js 18+
- Internet access for dependencies

## Backend (api/)
- Run: `cd api && ./mvnw spring-boot:run`
- Test: `cd api && ./mvnw test`
- Build: `cd api && ./mvnw clean package`
- Default dev DB: in-memory H2; console at `/h2-console`

### API Endpoints (base `/codex-example/api/v1`)
- `POST /employees` Create employee `{ name, email }`
- `GET /employees` List employees
- `POST /timesheets` Create/update week `{ employeeId, weekStart, entries[] }`
- `PUT /timesheets/{id}` Update entries
- `GET /reports/summary?start=YYYY-MM-DD&end=YYYY-MM-DD` Weekly totals

## Frontend (ui/)
- Install: `cd ui && npm install`
- Dev: `cd ui && npm run dev` (http://localhost:5173)
- Test: `cd ui && npm test`
- Build: `cd ui && npm run build`

Dev server proxies API calls: Vite is configured to proxy `'/codex-example/api/v1'` to `http://localhost:8080` during development. Run the backend on port 8080 for the UI to work locally.

The UI provides:
- Landing page with employee selector and weekly entry fields
- Add Employee prompt (name + email)
- Weekly summary loader for the current week

## Git & GitHub
- Conventional commits: `feat:`, `fix:`, `chore:`, `test:`
- Commit after completing each issue
- Push to `origin/main` (requires PAT via `GITHUB_TOKEN` if using MCP GitHub server)

## Notes
- DTOs are used; JPA entities are not exposed.
- Input validation via Jakarta Bean Validation.
- Integration tests use Spring Boot Test + MockMvc.
