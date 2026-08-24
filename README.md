# uptime-monitor

![CI](https://github.com/kbetkowsky/uptime-monitor/actions/workflows/ci.yml/badge.svg)

A small service that checks if websites are up. It runs checks every 60 seconds, stores the history and returns uptime statistics.

## Features

- Add websites to monitor over a REST API
- Automatic check every 60 seconds
- History of checks with HTTP status and response time
- Uptime percentage for the last 24 hours, cached in Redis

## Tech stack

Java 21, Spring Boot 4.1, PostgreSQL 16, Flyway, Redis, Docker Compose, JUnit 5, Mockito, Testcontainers, GitHub Actions.

## How it works

A scheduler runs every 60 seconds and loads all enabled sites from the database. For each site it sends an HTTP request with a 5 second connect and read timeout. The response code decides the status: 200-399 is UP, anything else is DOWN, and no response at all is also DOWN. Every check is saved as a new row, so history is never modified.

The uptime endpoint does not load all rows into memory. It uses one aggregate query with GROUP BY and returns only counters. The percentage is calculated in Java.

The project uses a layered structure: controller -> service -> repository.

## API

| Method | Path | Description |
|---|---|---|
| POST | /sites | Add a site to monitor |
| GET | /sites | List monitored sites |
| POST | /sites/{id}/check | Run a check for one site now |
| GET | /checks/recent | Last 50 checks |
| GET | /checks/uptime | Uptime for the last 24 hours |

## Running locally

```
docker compose up -d
```

Ports: application 8090, PostgreSQL 5441, Redis 6380.

Add a site:

```
curl -X POST http://localhost:8090/sites \
  -H "Content-Type: application/json" \
  -d '{"name":"Example","url":"https://example.com"}'
```

Wait one minute for the first check, then:

```
curl http://localhost:8090/checks/uptime
```

## Tests

```
./mvnw verify
```

- unit tests for the status rule and the uptime calculation
- service tests with Mockito
- repository tests with Testcontainers, which start a real PostgreSQL container

Docker is required to run the tests.

## Known limitations

- The check_results table grows without any limit. A production system would need data retention or hourly aggregates.
- A site with no checks in the last 24 hours does not appear in the uptime response, because the query uses an inner join. There is a test for this behaviour.
- Checks run one after another, so a full round takes longer when more sites are added. Virtual threads would be the next step.
- The uptime response is cached for 60 seconds, so the values can be up to one minute old.