# Volunteer Action Management System

Cloud-native Quarkus microservices application for managing volunteers, organizations, volunteer actions, and participations.

## Architecture

The application is a Maven multi-module project with three independently deployable services:

| Service | Port | Responsibility | Database |
| --- | --- | --- | --- |
| `user-service` | `8081` | Volunteer and organization registration, authentication, profile management | H2 |
| `action-service` | `8082` | Volunteer action CRUD and search | H2 |
| `participation-service` | `8083` | Participation registration, availability checks, confirmation notifications | H2 |

Rules followed:

- Each service owns its own H2 database.
- Services communicate only through REST APIs.
- DTOs are used at API boundaries.
- Each service uses `resource`, `service`, `repository`, `entity`, and `dto` packages.
- `participation-service` calls `user-service` and `action-service` through MicroProfile REST Client.

## Technology

- Java 21
- Quarkus
- Maven multi-module build
- Jakarta REST / Quarkus REST with JSON-B
- Hibernate ORM with Panache
- H2 database
- MicroProfile Config
- MicroProfile REST Client
- MicroProfile Health, Fault Tolerance, Metrics and OpenTelemetry
- SmallRye OpenAPI and Swagger UI
- JUnit 5, Rest-Assured, Mockito
- Docker and Docker Compose

## Build And Test

```bash
mvn clean test
```

Package the services:

```bash
mvn clean package
```

## Run Locally

Run each service in a separate terminal:

```bash
mvn -pl user-service quarkus:dev
mvn -pl action-service quarkus:dev
mvn -pl participation-service quarkus:dev
```

Swagger UI:

- User service: http://localhost:8081/q/swagger-ui
- Action service: http://localhost:8082/q/swagger-ui
- Participation service: http://localhost:8083/q/swagger-ui

OpenAPI JSON/YAML:

- http://localhost:8081/q/openapi
- http://localhost:8082/q/openapi
- http://localhost:8083/q/openapi

## Docker

Build the Quarkus applications first:

```bash
mvn clean package
```

Then start all services:

```bash
docker compose up --build
```

Docker Compose also starts Jaeger at http://localhost:16686. Health and metrics
are available at:

- http://localhost:8081/q/health/ready and http://localhost:8081/q/metrics
- http://localhost:8082/q/health/ready and http://localhost:8082/q/metrics
- http://localhost:8083/q/health/ready and http://localhost:8083/q/metrics

## Minikube And Observability

Kubernetes manifests and the scenario report for Health, Fault Tolerance, Metrics and Jaeger tracing are documented in [docs/cloud-deployment-report.md](docs/cloud-deployment-report.md).

## Example Curl Commands

Create an organization:

```bash
curl -i -X POST http://localhost:8081/organizations \
  -H "Content-Type: application/json" \
  -d '{
    "username": "open-aid",
    "email": "contact@openaid.example",
    "password": "secret1",
    "afm": "123456789",
    "organizationName": "Open Aid",
    "description": "Community support organization",
    "mission": "Coordinate meaningful volunteer action",
    "foundedYear": 2015,
    "address": "1 Solidarity St",
    "city": "Athens",
    "postalCode": "10431",
    "phone": "+302101234567"
  }'
```

Create a volunteer:

```bash
curl -i -X POST http://localhost:8081/volunteers \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ada",
    "email": "ada@example.com",
    "password": "secret1",
    "firstName": "Ada",
    "lastName": "Lovelace",
    "city": "Athens"
  }'
```

Authenticate:

```bash
curl -i -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "ada", "password": "secret1"}'
```

Create an activism action:

```bash
curl -i -X POST http://localhost:8082/actions \
  -H "Content-Type: application/json" \
  -d '{
    "type": "ACTIVISM",
    "title": "Park cleanup",
    "description": "Clean and restore the local park",
    "startDate": "2026-07-01T10:00:00",
    "endDate": "2026-07-01T14:00:00",
    "location": "Athens",
    "category": "environment",
    "minParticipants": 5,
    "maxParticipants": 25
  }'
```

Search actions:

```bash
curl -i "http://localhost:8082/actions?category=environment&location=Athens"
```

Create a participation:

```bash
curl -i -X POST http://localhost:8083/participations \
  -H "Content-Type: application/json" \
  -d '{"volunteerId": 1, "actionId": 1}'
```

List volunteer participations:

```bash
curl -i http://localhost:8083/participations/volunteer/1
```

Cancel participation:

```bash
curl -i -X DELETE http://localhost:8083/participations/1
```

## API Summary

`user-service`:

- `POST /organizations`
- `POST /volunteers`
- `GET /users/{id}`
- `PUT /users/{id}`
- `POST /auth/login`

`action-service`:

- `POST /actions`
- `GET /actions`
- `GET /actions/{id}`
- `PUT /actions/{id}`
- `DELETE /actions/{id}`

`participation-service`:

- `POST /participations`
- `DELETE /participations/{id}`
- `GET /participations/volunteer/{id}`
