# Cloud deployment, observability and fault tolerance

This report documents the MicroProfile/Quarkus support added for running the volunteer microservices on minikube.

## Implemented APIs

- Health API: each service exposes `/q/health/live` and `/q/health/ready`. The readiness check opens a JDBC connection and reports the database status.
- Fault Tolerance API: REST clients between services use `@Timeout`, `@Retry` and `@CircuitBreaker`.
- Metrics API: selected public API methods use MicroProfile `@Counted` and `@Timed`. Metrics are exposed at `/metrics`.
- OpenTelemetry API: traces are exported through OTLP to Jaeger.

## Build container images

Run from the repository root:

```powershell
.\apache-maven-3.9.16\bin\mvn.cmd clean package -DskipTests
docker build -t volunteer/user-service:1.0.3 .\user-service
docker build -t volunteer/action-service:1.0.3 .\action-service
docker build -t volunteer/participation-service:1.0.3 .\participation-service
```

## Run with Docker Compose

Docker Compose starts the three services plus Jaeger for local tracing. Each service
gets its own H2 database file under `/deployments/data`, exports traces to Jaeger,
and exposes readiness health checks.

```powershell
.\apache-maven-3.9.16\bin\mvn.cmd clean package -DskipTests
docker compose up --build
```

Useful endpoints:

- User health: `http://localhost:8081/q/health/ready`
- Action health: `http://localhost:8082/q/health/ready`
- Participation health: `http://localhost:8083/q/health/ready`
- User metrics: `http://localhost:8081/metrics`
- Action metrics: `http://localhost:8082/metrics`
- Participation metrics: `http://localhost:8083/metrics`
- Jaeger UI: `http://localhost:16686`

For minikube, either build inside the minikube Docker daemon or load the images:

```powershell
minikube image load volunteer/user-service:1.0.3
minikube image load volunteer/action-service:1.0.3
minikube image load volunteer/participation-service:1.0.3
```

## Deploy on minikube

```powershell
minikube start
kubectl apply -f k8s/minikube.yml
kubectl get pods
kubectl port-forward service/user-service 8081:8081
kubectl port-forward service/action-service 8082:8082
kubectl port-forward service/participation-service 8083:8083
kubectl port-forward service/jaeger 16686:16686
```

Jaeger UI: `http://localhost:16686`.

## Scenario 1: normal distributed operation

1. Create an organization through `user-service`.
2. Create a volunteer through `user-service`.
3. Create an action through `action-service`.
4. Register a participation through `participation-service`.

Expected result:

- All API calls return successful responses.
- `/q/health/ready` is `UP` for all services.
- `/metrics` contains the custom counters and timers.
- Jaeger contains traces that include the participating services.

Suggested screenshots:

- `kubectl get pods`
- `/q/health/ready` for each service
- `/metrics` showing `actions_created_total`, `volunteers_created_total` or `participations_created_total`
- Jaeger trace for the participation request

## Scenario 2: slow downstream service

Patch the shared configuration to delay all business endpoints in minikube:

```powershell
kubectl patch configmap volunteer-config --type merge -p '{"data":{"VOLUNTEER_SIMULATION_DELAY_MS":"2000","VOLUNTEER_SIMULATION_FAILURE_RATE":"0"}}'
kubectl rollout restart deployment/user-service deployment/action-service deployment/participation-service
```

For Docker Compose, change `VOLUNTEER_SIMULATION_DELAY_MS` to `2000` in
`docker-compose.yml` and restart with `docker compose up --build`.

Call an endpoint that crosses service boundaries, for example creating a participation. The REST client timeout is `1000 ms`, so slow downstream calls should fail fast and then retry according to the configured `@Retry` policy.

Expected result:

- The caller eventually receives an error response instead of waiting indefinitely.
- Jaeger shows retry/failed spans around the slow downstream request.
- Metrics timers show increased latency.

Reset:

```powershell
kubectl patch configmap volunteer-config --type merge -p '{"data":{"VOLUNTEER_SIMULATION_DELAY_MS":"0"}}'
kubectl rollout restart deployment/user-service deployment/action-service deployment/participation-service
```

## Scenario 3: random service failures

Enable random failures in minikube:

```powershell
kubectl patch configmap volunteer-config --type merge -p '{"data":{"VOLUNTEER_SIMULATION_FAILURE_RATE":"0.5"}}'
kubectl rollout restart deployment/user-service deployment/action-service deployment/participation-service
```

For Docker Compose, change `VOLUNTEER_SIMULATION_FAILURE_RATE` to `0.5` in
`docker-compose.yml` and restart with `docker compose up --build`.

Run the same cross-service request several times.

Expected result:

- Some calls succeed after retry.
- Some calls fail with service-unavailable behavior after retries are exhausted or after the circuit breaker opens.
- Jaeger displays failed spans, and `/metrics` shows the request counters continuing to increase.

Reset:

```powershell
kubectl patch configmap volunteer-config --type merge -p '{"data":{"VOLUNTEER_SIMULATION_FAILURE_RATE":"0"}}'
kubectl rollout restart deployment/user-service deployment/action-service deployment/participation-service
```

## Verified minikube execution - June 18, 2026

The repository manifest was applied with the three `1.0.3` service images and
`jaegertracing/all-in-one:1.57`.

Verified Kubernetes state:

```text
action-service          1/1 Running, image volunteer/action-service:1.0.3
participation-service   1/1 Running, image volunteer/participation-service:1.0.3
user-service            1/1 Running, image volunteer/user-service:1.0.3
jaeger                  1/1 Running, image jaegertracing/all-in-one:1.57
```

Verified from inside each service pod:

- `GET /` returned HTTP `200` and the correct service welcome page.
- `GET /q/health/live` returned `UP`.
- `GET /q/health/ready` returned `UP`, including the database checks.
- `GET /metrics` returned HTTP `200`.
- The metrics output contained application counters/timers such as
  `application_actions_availability_checked_total`,
  `application_organizations_create_seconds_*`, and
  `application_participations_cancel_seconds_*`.

The full cross-service normal, delay, random-failure, and Jaeger trace scenarios
still need to be executed and captured. They are not marked as verified in this
report.
