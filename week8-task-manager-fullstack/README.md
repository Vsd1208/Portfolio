# Week 8 — Full-Stack Task Manager

A complete React TypeScript and Spring Boot integration project. Flowboard provides JWT authentication, private task boards, CRUD operations, drag-and-drop status changes, responsive design, and live WebSocket refreshes.

## Features

- Registration and login with BCrypt passwords
- Short-lived JWT access tokens and seven-day refresh tokens
- Axios service layer with automatic token refresh
- User-scoped task CRUD with status, priority, and due dates
- Drag tasks between To do, In progress, and Done
- Search, loading skeletons, optimistic status updates, and API error feedback
- STOMP/WebSocket task notifications
- CORS and environment-based frontend/API configuration
- PostgreSQL, Docker Compose, and an H2-backed Spring context test

## Architecture

```text
React UI -> Axios + JWT -> Spring Security -> REST controllers -> JPA -> PostgreSQL
    ^                              |
    `------- STOMP /topic/tasks ---'
```

The backend never accepts an owner ID from the client. It derives the current user from the verified access token and scopes every task query and mutation to that user.

## Quick start with Docker

```bash
docker compose up --build
```

Open `http://localhost:3000`. The API runs at `http://localhost:8080`.

For a real deployment, set a long random `JWT_SECRET` before starting the containers.

## Manual development

Start PostgreSQL, then:

```bash
cd backend
mvn spring-boot:run
```

In another terminal:

```bash
cd frontend
npm install
npm run dev
```

Copy `frontend/.env.example` to `frontend/.env` when endpoints differ from the defaults.

## API

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Create an account and return tokens |
| POST | `/api/auth/login` | Authenticate and return tokens |
| POST | `/api/auth/refresh` | Exchange a refresh token |
| GET | `/api/tasks` | List the current user's tasks |
| POST | `/api/tasks` | Create a task |
| PUT | `/api/tasks/{id}` | Edit a task |
| PUT | `/api/tasks/{id}/status` | Move a task |
| DELETE | `/api/tasks/{id}` | Delete a task |

Protected endpoints require `Authorization: Bearer <accessToken>`.

## Verification

```bash
cd backend && mvn test
cd frontend && npm install && npm run build
```

## Project structure

```text
week8-task-manager-fullstack/
|-- backend/
|   |-- src/main/java/com/taskmanager/
|   |   |-- config/
|   |   |-- controller/
|   |   |-- dto/
|   |   |-- model/
|   |   |-- repository/
|   |   `-- security/
|   |-- src/main/resources/application.yml
|   |-- src/test/
|   |-- Dockerfile
|   `-- pom.xml
|-- frontend/
|   |-- src/components/
|   |-- src/hooks/
|   |-- src/services/
|   |-- Dockerfile
|   `-- package.json
|-- docker-compose.yml
`-- README.md
```

## Quality checklist

- [x] React 18 with TypeScript and Vite
- [x] Spring Boot REST backend
- [x] JWT access and refresh tokens
- [x] Task CRUD and drag-and-drop
- [x] Real-time WebSocket updates
- [x] Responsive custom UI
- [x] Loading and error states
- [x] Environment configuration
- [x] Docker containerization
- [x] Backend smoke test
