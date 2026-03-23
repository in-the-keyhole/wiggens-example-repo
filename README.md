# Timesheet System

This repository hosts a full-stack timesheet application with a Spring Boot REST API backend and a React + Vite frontend.

## Project Structure

```
api/    # Spring Boot backend (Maven)
ui/     # React frontend (Vite + TypeScript)
```

## Prerequisites

- Java 17+
- Node.js 18+
- npm 9+

## Backend (api)

```
cd api
./mvnw spring-boot:run       # Start the API on http://localhost:8080
./mvnw test                  # Run backend tests
./mvnw clean package         # Build the executable jar
```

Key endpoints are exposed under `/codex-example/api/v1/` and documented in the controllers.

## Frontend (ui)

```
cd ui
npm install                  # Install dependencies
npm run dev                  # Start Vite dev server on http://localhost:5173
npm run build                # Create production build
npm test                     # Run frontend unit tests (Vitest)
```

The UI expects the backend to run on `http://localhost:8080`. Override the API base URL by setting `VITE_API_BASE_URL` in a `.env` file if needed.

## Development Notes

- Follow the layered architecture described in `Agents.md`.
- DTOs isolate REST payloads from JPA entities.
- Controller integration tests live in `api/src/test/java/...`.
- React components use MUI and custom hooks in `ui/src/hooks` for data access.
