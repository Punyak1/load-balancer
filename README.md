# Load Balancer Project

## Overview
This project implements a simple load balancer using Java Spring Boot. It distributes HTTP requests across backend servers using various algorithms like round-robin (default) and random selection. The implementation supports:

- Dynamic server registration and removal.
- Switching between load balancing algorithms.
- Handling concurrent requests efficiently.
- Error handling and monitoring.

## Features
1. **Algorithms**:
   - Round-robin (default): Distributes requests sequentially across available servers.
   - Random: Distributes requests to a randomly selected server.

2. **Dynamic Server Management**:
   - Servers can be added or removed dynamically using APIs.

3. **Health Monitoring**:
   - API to view the current state of registered servers.

4. **Switchable Algorithms**:
   - API to switch between supported load balancing algorithms.

## Setup Instructions

### Prerequisites
- Java 17+
- Gradle 8.11.1
- springboot 3.4.1

### Build and Run

1. Build the project using Gradle:
   ```bash
   ./gradlew clean build
   ```

2. Run the application:
   ```bash
   ./gradlew bootRun
   ```

### API Endpoints

| Method | Endpoint         | Description                                             |
|--------|------------------|---------------------------------------------------------|
| GET    | `/api/request`   | Forwards a request to a backend server.                 |
| POST   | `/api/register`  | Registers a new backend server.                         |
| POST   | `/api/remove`    | Removes a backend server.                               |
| GET    | `/api/health`    | Lists all registered servers with their health status.  |
| POST   | `/api/algorithm` | Switches the load balancing algorithm.                  |

### Example Requests

**Register a Server**:
```bash
curl -X POST "http://localhost:8080/api/register?serverUrl=http://www.example.com"
```

**Remove a Server**:
```bash
curl -X POST "http://localhost:8080/api/remove?serverUrl=http://www.example.com"
```

**Switch Algorithm**:
```bash
curl -X POST "http://localhost:8080/api/algorithm?algorithm=random"
```

**Forward a Request**:
```bash
curl -X GET "http://localhost:8080/api/request"
```

**Check Health**:
```bash
curl -X GET "http://localhost:8080/api/health"
```

## Design Documentation

### High-Level Design
- The application listens for incoming HTTP requests and distributes them among backend servers based on the selected algorithm.
- It provides REST APIs to manage servers and control the load balancing behavior dynamically.

### Low-Level Design
- **Controller**:
  Handles incoming REST API requests.
- **Service Factory**:
  Dynamically provides the appropriate load balancing algorithm implementation.
- **Services**:
  Implements specific algorithms (e.g., round-robin, random).
- **ServerInstance**:
  Represents a backend server with its URL.

### Extensibility
- New algorithms can be added by implementing the `LoadBalancerService` interface and registering the implementation with the `LoadBalancerServiceFactory`.

## Future Enhancements
1. **Health Checks**:
   - Periodic health checks to detect server failures and remove/recover servers dynamically.
2. **Additional Algorithms**:
   - Add more load balancing strategies like least connections or weighted round-robin.
3. **Metrics and Monitoring**:
   - Integrate with monitoring tools like Prometheus or Grafana for better observability.

