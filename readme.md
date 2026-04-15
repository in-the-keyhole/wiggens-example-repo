# Ralph Timesheet

Monorepo for a full‑stack example application with a Spring Boot backend and a React + Vite + TypeScript frontend. The project follows a clean, layered architecture and provides local development using H2 with an option to use PostgreSQL for production.

## Tech Stack
- Backend: Spring Boot 3, Spring Data JPA, H2 (dev), PostgreSQL (prod), Maven, Lombok
- Frontend: React 18, Vite, TypeScript, MUI
- Testing: JUnit 5 + Spring Boot Test (backend), Vitest + React Testing Library (frontend)

## Repository Structure
```
ralph-timesheet/
  api/          # Spring Boot backend (Maven project)
  ui/           # React frontend (Vite + TypeScript)
  Agents.md     # Project instructions
  PROMPT.md     # Ralph loop prompt
```

## Prerequisites
- Java 17+
- Maven 3.9+ (or use Maven Wrapper if added)
- Node.js 18+ and npm 9+

## Quick Start

### 1) Backend (API)
- Run (dev, H2):
  - `cd api && mvn spring-boot:run`
- Tests:
  - `cd api && mvn test`
- Build JAR:
  - `cd api && mvn clean package`

Defaults
- Server: `http://localhost:8080`
- Base API path: `/codex-example/api/v1` (configurable via `app.api-prefix`)
- H2 Console: `http://localhost:8080/h2-console` (user: `sa`, password: `password`)

Configuration
- File: `api/src/main/resources/application.yml`
- To use PostgreSQL (example):
  ```yaml
  spring:
    datasource:
      url: jdbc:postgresql://localhost:5432/yourdb
      username: youruser
      password: yourpass
    jpa:
      hibernate:
        ddl-auto: update
  ```
  You can also set these via environment variables (e.g., `SPRING_DATASOURCE_URL`).

### 2) Frontend (UI)
- Install deps:
  - `cd ui && npm install`
- Start Dev Server:
  - `cd ui && npm run dev` (serves on `http://localhost:5173`)
- Tests:
  - `cd ui && npm test`
- Production Build:
  - `cd ui && npm run build`

The UI should call the API at `http://localhost:8080/codex-example/api/v1` during local development.

## Development Conventions

### Backend
- Layered architecture: Controller → Service → Repository
- REST endpoints under `/codex-example/api/v1/`
- Use DTOs for request/response — do not expose JPA entities directly
- Validate inputs with Jakarta Bean Validation annotations
- Write integration tests using `@WebMvcTest` or `@SpringBootTest`
- Use Lombok for boilerplate reduction (`@Data`, `@Builder`, etc.)

### Frontend
- Functional components with hooks
- Colocate files:
  - `ComponentName/index.tsx`
  - `ComponentName/ComponentName.test.tsx`
- Use a shared API module for backend calls (Axios instance with base URL), e.g. `codex-example/api/`
- Keep business logic out of components — prefer custom hooks
- Use MUI components and theming

## Testing
- Backend: `cd api && mvn test`
  - JUnit 5 + Spring Boot Test
- Frontend: `cd ui && npm test`
  - Vitest + React Testing Library (happy‑dom)

## Useful Paths
- API config: `api/src/main/resources/application.yml`
- UI entry: `ui/src/main.tsx`
- Example pages: `ui/src/pages/`

## Troubleshooting
- Port conflicts:
  - API uses `8080` (override with `SERVER_PORT` or `server.port` in config)
  - UI uses `5173` (Vite can be configured via `vite.config.ts`)
- H2 Console not loading:
  - Confirm `spring.h2.console.enabled=true` (enabled by default in `application.yml`)
- CORS issues:
  - Add/adjust CORS configuration in the backend if UI cannot reach the API in dev

## Git
- Commit: `git commit`
- Push: `git push`

## License
This project is provided as an example for educational and internal development purposes.

