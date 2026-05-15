# Intelligent Scheduling Assistant System 🤖🗓️

> **AI-powered scheduling and task orchestration backend** built with Java 21 and Spring Boot, designed to detect conflicts, optimize availability, coordinate real-time reminders, and support secure multi-session authentication at scale.

---

## 📌 Project Introduction

The **Intelligent Scheduling Assistant System** is a production-oriented backend platform for intelligent task planning, schedule coordination, and AI-assisted productivity automation.

This project goes far beyond a standard CRUD application. It is engineered as a **backend-first scheduling engine** with a strong focus on:

- clean architecture and separation of concerns
- secure JWT-based authentication with refresh token rotation
- conflict-aware scheduling and time-slot optimization
- asynchronous reminder processing and background job orchestration
- real-time event delivery through WebSocket communication
- scalable persistence with PostgreSQL, JPA/Hibernate, and Redis

The system is built to help users manage tasks, recurring schedules, reminders, and natural-language scheduling requests with intelligent backend logic rather than simple data storage.

---

## ✨ Key Features

- 🧠 **AI-assisted scheduling recommendations** based on availability, urgency, and priority
- 📅 **Task and schedule management** with recurring support
- ⚠️ **Conflict detection engine** to identify overlapping events and time clashes
- ⏳ **Free-time slot suggestion** for optimal productivity planning
- 🔔 **Realtime notifications and reminders** via WebSocket
- 🔐 **JWT authentication with refresh token rotation** for secure multi-session access
- 🧩 **Layered Clean Architecture** with Domain / Application / Infrastructure separation
- 🧵 **Quartz-based background jobs** for scheduled execution and async reminder workflows
- 🚀 **Redis-backed caching/session strategy** for performance and session durability
- 🗃️ **PostgreSQL persistence** with indexing and transactional consistency in mind
- 🧯 **Optimistic locking and concurrency handling** for safe schedule updates
- 🧾 **Audit logging support** for traceability and operational visibility

---

## 🏗️ Architecture Overview

This codebase follows a **Clean Architecture** style to keep business rules independent from frameworks and infrastructure concerns.

### Layer responsibilities

#### 1) `domain`
The core business layer.

- entities: `User`, `Task`, `Schedule`
- enums: `Priority`, `TaskStatus`, `ScheduleStatus`, `RecurrenceType`, `SenderType`
- domain rules, invariants, and scheduling logic
- exception types for business-level failures

#### 2) `application`
The orchestration layer.

- DTOs for input/output contracts
- use-case style services
- mappers for translating between domain and transport models

#### 3) `infrastructure`
The technical implementation layer.

- persistence adapters and repository implementations
- security configuration and JWT infrastructure
- external integrations
- framework-level configuration

#### 4) `presentation`
The API delivery layer.

- REST controllers
- global advice / exception handling
- WebSocket endpoints and real-time message delivery

### Architectural goals

- keep business rules framework-agnostic
- improve testability and maintainability
- isolate infrastructure complexity
- support feature growth without collapsing into controller/service clutter
- make the backend easier to evolve for production-scale use cases

---

## 🧠 Scheduling Engine Explanation

The scheduling engine is the core value of this system. Instead of storing tasks passively, the backend actively reasons about time, availability, and conflict risk.

### Engine capabilities

- checks for overlapping schedules and time window collisions
- evaluates user priority and task urgency
- identifies valid free slots for new scheduling requests
- supports recurring schedule generation and rule-based repetition
- triggers reminder jobs at the correct execution time
- adapts scheduling suggestions based on current workload and calendar density

### Conflict detection strategy

The backend applies a deterministic conflict-checking flow before schedule confirmation:

1. normalize requested time ranges
2. fetch existing schedules for the affected user range
3. compare interval overlap conditions
4. block invalid writes or suggest alternate time windows
5. persist only conflict-safe schedules

This approach keeps scheduling logic predictable, auditable, and safe under concurrent updates.

---

## 🤖 AI Assistant Workflow

The AI assistant layer is designed to behave like a scheduling co-pilot rather than a chatbot toy.

### Workflow summary

1. **User submits natural language intent**
   - e.g. “Schedule a deep work session tomorrow afternoon”

2. **Intent is interpreted by the backend workflow**
   - task type, urgency, duration, recurrence, and constraints are extracted

3. **Availability and conflict checks are performed**
   - the engine scans existing tasks and schedules

4. **Optimized recommendation is generated**
   - the assistant proposes the best available time slot(s)

5. **Schedule is confirmed and persisted**
   - notifications, audit logs, and reminder jobs are triggered

6. **Realtime updates are broadcast**
   - WebSocket messages inform the client immediately

### Why this matters

This workflow demonstrates a backend that can combine **rule-based scheduling**, **async job execution**, and **AI-assisted decision support** in one cohesive pipeline.

---

## 🔐 Security Features

Security is designed as a first-class backend concern.

### Authentication and session model

- **JWT authentication** for stateless access control
- **Refresh token rotation** to reduce token replay risk
- support for **multiple active sessions** per user
- token-based secure API access for frontend and automation clients

### Spring Security architecture

- authentication and authorization are centrally enforced
- protected endpoints are separated from public entry points
- security rules are designed to be extensible for future roles and permissions

### Security engineering highlights

- refresh-token lifecycle management
- session invalidation support
- safer long-lived access through token renewal patterns
- backend-friendly design for distributed authentication scenarios

---

## 🗃️ Database Design Highlights

The persistence layer is designed for scheduling workloads, not generic toy data.

### Core persistence choices

- **PostgreSQL** as the primary relational database
- **JPA/Hibernate** for ORM and transactional persistence
- **Redis** for caching and fast session/state support

### Database engineering considerations

- optimized indexing strategy for schedule lookups and user-based queries
- conflict-heavy time-range queries are kept efficient
- transactional consistency for schedule creation and updates
- optimistic locking to reduce race conditions during concurrent modifications
- audit-friendly data modeling for operational traceability

### Why PostgreSQL fits this project

Scheduling systems depend on reliable joins, time-range filtering, and transactional integrity. PostgreSQL provides the consistency and query performance needed for production-grade coordination logic.

---

## 📡 Realtime Communication

WebSocket is used for low-latency backend-to-client communication.

### Real-time use cases

- schedule creation confirmations
- reminder notifications
- conflict warnings
- AI recommendation responses
- background job status updates

This makes the system feel responsive and operationally modern, especially for reminder-driven workflows.

---

## ⚙️ Quartz Background Jobs & Async Processing

The backend uses **Quartz Scheduler** for time-based automation and reminder execution.

### Background job responsibilities

- dispatch scheduled reminders
- process delayed or recurring scheduling actions
- support async backend workflows without blocking user requests
- keep execution logic decoupled from synchronous API calls

### Engineering value

- reduces pressure on request-response flows
- enables reliable reminder delivery
- fits naturally with production job orchestration patterns
- supports future scaling for more complex automation pipelines

---

## 🚀 Redis Caching Strategy

Redis is positioned as a fast operational layer for high-frequency backend needs.

### Common Redis use cases in this system

- session support for multi-session authentication
- caching frequently accessed scheduling data
- short-lived state for reminder and notification workflows
- performance acceleration for repeated backend reads

Redis helps keep the system responsive while preserving PostgreSQL as the source of truth.

---

## 🧱 Tech Stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot |
| Security | Spring Security, JWT, Refresh Token Rotation |
| Persistence | JPA / Hibernate, PostgreSQL |
| Cache / Session Store | Redis |
| Realtime Communication | WebSocket |
| Scheduling | Quartz Scheduler |
| Containerization | Docker |
| Build Tool | Maven |
| Architecture | Clean Architecture, Layered Backend Design |

---

## 📁 Folder Structure

```text
src/main/java/com/example/intelligent_scheduler/
├── application/
│   ├── dto/
│   ├── mapper/
│   └── service/
├── domain/
│   ├── entity/
│   ├── enums/
│   └── exception/
├── infrastructure/
│   ├── config/
│   ├── external/
│   ├── persistence/
│   └── security/
└── presentation/
	├── advice/
	├── controller/
	└── websocket/
```

### Domain model snapshot

- `User` → identity and access ownership
- `Task` → user work items and scheduling intent
- `Schedule` → actual planned time blocks
- `Priority` → task importance weighting
- `TaskStatus` → task lifecycle state
- `ScheduleStatus` → schedule execution state
- `RecurrenceType` → repeat patterns
- `SenderType` → message origin tracking

---

## 🧩 API Modules

The backend is organized by responsibility, not by UI screens.

### Expected API modules

- **Authentication Module**
  - login, token refresh, session handling, logout

- **User Module**
  - user identity, profile-level schedule ownership

- **Task Module**
  - task creation, updates, priority handling, status tracking

- **Schedule Module**
  - schedule creation, conflict detection, recurrence, free-slot suggestions

- **Reminder / Notification Module**
  - async reminders, notification delivery, WebSocket push events

- **AI Assistant Module**
  - natural language scheduling requests and recommendations

- **Health / Ops Module**
  - application health, observability, and production monitoring support

---

## 🛠️ Setup Instructions

### Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL and Redis running locally or via Docker

### Run the infrastructure

```powershell
docker compose -f compose.yaml up -d
```

This starts:

- PostgreSQL on `localhost:5432`
- Redis on `localhost:6379`
- pgAdmin on `localhost:5050`

### Run the application

```powershell
.\mvnw spring-boot:run
```

### Build the project

```powershell
.\mvnw clean package
```

### Useful notes

- Application metadata is defined in `src/main/resources/application.properties`
- Docker Compose is already included for local infrastructure bootstrapping
- The project is structured to support extension into more advanced deployment pipelines

---

## 🐳 Docker Setup

The repository includes a `compose.yaml` file for local development infrastructure.

### Services included

- **PostgreSQL 15 Alpine** — primary database
- **Redis 7 Alpine** — cache/session store
- **pgAdmin 4** — lightweight database administration UI

### Why this is useful

- reproducible local setup
- fast onboarding for new developers and reviewers
- infrastructure parity between local development and production-style environments

---

## 🌟 What Makes This Different from a Typical CRUD Project?

Most CRUD projects stop at basic create/read/update/delete flows.

This project is different because it demonstrates **backend engineering depth**:

- it makes scheduling decisions using domain logic, not just database writes
- it handles **time conflicts**, **free-slot search**, and **recurrence rules**
- it uses **JWT + refresh token rotation** instead of simplistic auth
- it combines **Redis**, **Quartz**, and **WebSocket** for a more realistic backend system
- it is designed around **clean architecture**, not controller-heavy shortcuts
- it considers **concurrency, auditability, and persistence efficiency**
- it introduces an **AI-assisted workflow** for natural scheduling requests

In short, this is a **systems-oriented backend project** that reflects real-world product and platform thinking.

---

## 🧭 Why This Project Is Technically Challenging

This system requires more than just API wiring. It involves multiple backend concerns working together:

- time-based conflict detection with edge-case handling
- safe concurrent schedule updates under transactional load
- secure token lifecycle management across multiple sessions
- event-driven communication with real-time delivery guarantees
- background job execution without blocking the request pipeline
- cache consistency between Redis and PostgreSQL
- extensible architecture for AI-assisted scheduling decisions

These are the kinds of problems that appear in production systems, which makes the project highly relevant for backend engineering interviews and internships.

---

## 🔮 Future Improvements

- calendar integration with external providers
- advanced availability scoring and scheduling heuristics
- analytics dashboard for productivity insights
- event sourcing or domain event pipelines
- richer audit trails and admin observability
- notification preferences and channel-based routing
- distributed job execution support
- improved AI inference and prompt orchestration layer

---

## 👨‍💻 Engineering Mindset

This project reflects a backend mindset focused on:

- domain clarity
- operational reliability
- scalability
- maintainability
- security-first design
- asynchronous execution
- real-time user experience

It is built to communicate that the developer understands how to structure a serious backend system, not just implement endpoints.

---

## 📄 License

This project is available for learning, portfolio, and evaluation purposes.

---

> **Built to showcase production-grade backend thinking, intelligent scheduling logic, and modern Java architecture.**

