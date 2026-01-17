# 🏗️ Microservices Architecture - Student Management System

## 📋 Project Overview

This project demonstrates a complete migration from a Spring Boot monolith to a microservices architecture, implementing all required patterns and technologies for the DS Architecture des Composants course.

## 🎯 Objectives Completed

### ✅ Microservices Architecture with Spring Cloud
- **Config Server**: Centralized configuration management (port 8888)
- **Eureka Server**: Service discovery and registration (port 8761)
- **API Gateway**: Central routing and entry point (port 8080)
- **Student Service**: Manages student data (port 8089)
- **Department Service**: Manages department data (port 8088)
- **Enrollment Service**: CQRS/Event Sourcing with Axon (port 8090)

### ✅ Resilience Patterns (Resilience4j)
All patterns configured in [config-repo/student-microservice-app.yml](config-repo/student-microservice-app.yml):
- **Circuit Breaker**: Prevents cascade failures (50% failure threshold)
- **Retry**: 3 attempts with exponential backoff (500ms base delay)
- **Rate Limiter**: 5 requests/second limit

### ✅ Monitoring & Observability
- **Prometheus**: Metrics collection (port 9090)
- **Grafana**: Metrics visualization (port 3000, admin/admin)
- **Zipkin**: Distributed tracing (port 9411)
- **Micrometer**: Metrics instrumentation

### ✅ CQRS & Event Sourcing
- **Axon Framework**: Command/Query separation
- **Axon Server**: Event store (ports 8024, 8124)
- Separate read/write models
- Event-driven architecture

## 🏛️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         API Gateway (8080)                      │
│                      Zipkin Tracing Enabled                     │
└────────────────────────────┬────────────────────────────────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
┌───────▼───────┐   ┌───────▼────────┐   ┌──────▼──────────┐
│  Student      │   │  Department    │   │  Enrollment     │
│  Service      │──▶│  Service       │   │  Service        │
│  (8089)       │   │  (8088)        │   │  (8090)         │
│               │   │                │   │  CQRS + Axon    │
│ Resilience4j  │   │ Resilience4j   │   │                 │
│ - Circuit     │   │ - Circuit      │   │                 │
│ - Retry       │   │ - Retry        │   │                 │
│ - RateLimit   │   │ - RateLimit    │   │                 │
└───────┬───────┘   └────────┬───────┘   └────────┬────────┘
        │                    │                     │
        │                    │                     │
┌───────▼────────┐  ┌────────▼────────┐  ┌────────▼─────────┐
│  MySQL         │  │  MySQL          │  │  MySQL           │
│  studentdb     │  │  departmentdb   │  │  enrollmentdb    │
│  (3307)        │  │  (3308)         │  │  (3309)          │
└────────────────┘  └─────────────────┘  └──────────────────┘
                                                   │
                                          ┌────────▼──────────┐
                                          │  Axon Server      │
                                          │  Event Store      │
                                          │  (8024, 8124)     │
                                          └───────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    Infrastructure Services                       │
├─────────────────────────────────────────────────────────────────┤
│  Eureka (8761)  │  Config Server (8888)  │  Zipkin (9411)      │
│  Prometheus (9090)  │  Grafana (3000)                           │
└─────────────────────────────────────────────────────────────────┘
```

## 🚀 Quick Start Guide

### Prerequisites
- Java 17
- Maven 3.8+
- Docker & Docker Compose
- MySQL 8.0 (for local development)

### Option 1: Run with Docker Compose (Recommended)

1. **Build all services**:
```bash
# Build Eureka Server
cd discovery-service
mvn clean package -DskipTests
cd ..

# Build Config Server
cd config-server
mvn clean package -DskipTests
cd ..

# Build API Gateway
cd api-gateway
mvn clean package -DskipTests
cd ..

# Build Student Service
cd student-service
mvn clean package -DskipTests
cd ..

# Build Department Service
cd department-service
mvn clean package -DskipTests
cd ..

# Build Enrollment Service
cd enrollment-service
mvn clean package -DskipTests
cd ..
```

2. **Start all services**:
```bash
docker-compose up -d
```

3. **Access services**:
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080
- Zipkin: http://localhost:9411
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)
- Axon Server: http://localhost:8024

### Option 2: Run Locally

1. **Start infrastructure services**:
```bash
# Start MySQL instances
# Port 3306 for student DB
# Port 3307 for department DB
# Port 3308 for enrollment DB

# Start Eureka Server
cd discovery-service
mvn spring-boot:run

# Start Config Server (new terminal)
cd config-server
mvn spring-boot:run

# Start Axon Server (Docker)
docker run -d --name axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver

# Start Zipkin (Docker)
docker run -d --name zipkin -p 9411:9411 openzipkin/zipkin
```

2. **Start microservices** (wait for infrastructure to be ready):
```bash
# Start Department Service
cd department-service
mvn spring-boot:run

# Start Student Service (new terminal)
cd student-service
mvn spring-boot:run

# Start Enrollment Service (new terminal)
cd enrollment-service
mvn spring-boot:run

# Start API Gateway (new terminal)
cd api-gateway
mvn spring-boot:run
```

## 📡 API Endpoints

### Through API Gateway (http://localhost:8080)

#### Student Service
```bash
# Get all students
GET http://localhost:8080/students/getAllStudents

# Get student by ID
GET http://localhost:8080/students/getStudent/{id}

# Create student
POST http://localhost:8080/students/addStudent
Content-Type: application/json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "123456789",
  "departmentId": 1
}
```

#### Department Service
```bash
# Get all departments
GET http://localhost:8080/departments/getAllDepartments

# Get department by ID
GET http://localhost:8080/departments/getDepartment/{id}

# Create department
POST http://localhost:8080/departments/addDepartment
Content-Type: application/json
{
  "name": "Computer Science",
  "location": "Building A",
  "phone": "987654321",
  "head": "Dr. Smith"
}
```

#### Enrollment Service (CQRS)
```bash
# Create enrollment (Command)
POST http://localhost:8080/enrollments
Content-Type: application/json
{
  "studentId": 1,
  "courseId": 101
}

# Get enrollment by ID (Query)
GET http://localhost:8080/enrollments/{enrollmentId}

# Get enrollments by student (Query)
GET http://localhost:8080/enrollments/student/{studentId}

# Update enrollment status (Command)
PUT http://localhost:8080/enrollments/{enrollmentId}/status
Content-Type: application/json
{
  "status": "APPROVED"
}
```

## 🛡️ Resilience Patterns Demo

### 1. Circuit Breaker Test

**Scenario**: Department service is down

```bash
# Stop department service
docker stop department-service

# Try to create a student (will trigger fallback)
POST http://localhost:8080/students/addStudent
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane@example.com",
  "departmentId": 999
}

# Check logs - you'll see:
# - Circuit breaker opens after failures
# - Fallback method executes
# - Student saved without department validation
```

### 2. Retry Mechanism Test

**Scenario**: Transient network issues

```bash
# The retry mechanism automatically retries 3 times
# with exponential backoff (500ms, 1000ms, 2000ms)
# Check logs for retry attempts
```

### 3. Rate Limiter Test

**Scenario**: Too many requests

```bash
# Send 10 rapid requests
for i in {1..10}; do
  curl http://localhost:8080/students/getAllStudents &
done

# Requests exceeding 5/second will be rejected
# Check response: "Rate limit exceeded"
```

## 📊 Monitoring & Observability

### Prometheus Metrics

Access Prometheus: http://localhost:9090

**Useful queries**:
```promql
# Request rate
rate(http_server_requests_seconds_count[1m])

# Circuit breaker state
resilience4j_circuitbreaker_state

# Retry attempts
resilience4j_retry_calls_total

# JVM memory usage
jvm_memory_used_bytes
```

### Grafana Dashboards

1. Access Grafana: http://localhost:3000 (admin/admin)
2. Add Prometheus datasource (already configured)
3. Import dashboards:
   - Spring Boot Dashboard: ID 12900
   - Resilience4j Dashboard: ID 14889
   - JVM Dashboard: ID 4701

### Zipkin Distributed Tracing

1. Access Zipkin: http://localhost:9411
2. Select a service (e.g., student-service)
3. Click "Find Traces"
4. View request flow across services:
   - Gateway → Student Service → Department Service (via Feign)

## 🎓 CQRS & Axon Framework

### Architecture

**Command Side** (Write):
- `CreateEnrollmentCommand` → `EnrollmentAggregate` → `EnrollmentCreatedEvent`
- `UpdateEnrollmentStatusCommand` → `EnrollmentAggregate` → `EnrollmentStatusUpdatedEvent`

**Query Side** (Read):
- `EnrollmentProjection` listens to events
- Updates `EnrollmentQueryModel` (read model)
- Handles queries (`FindEnrollmentByIdQuery`, `FindEnrollmentsByStudentQuery`)

### Testing CQRS Flow

```bash
# 1. Create enrollment (Command)
POST http://localhost:8090/enrollments
{
  "studentId": 1,
  "courseId": 101
}
Response: "enrollment-id-123"

# 2. View Axon Server events
http://localhost:8024

# 3. Query enrollment (Query)
GET http://localhost:8090/enrollments/enrollment-id-123

# 4. Update status (Command)
PUT http://localhost:8090/enrollments/enrollment-id-123/status
{
  "status": "APPROVED"
}

# 5. Query again to see updated read model
GET http://localhost:8090/enrollments/enrollment-id-123
```

## 🔧 Configuration Management

### Config Server

All configurations are centralized in `config-repo/`:
- `student-microservice-app.yml`: Student service config + Resilience4j
- `department-microservice-app.yml`: Department service config

**Refresh configuration without restart**:
```bash
curl -X POST http://localhost:8089/actuator/refresh
```

## 🧪 Testing Scenarios

### Scenario 1: Service Unavailability
**Test**: Department service down, create student
**Expected**: Circuit breaker opens, fallback saves student without department

### Scenario 2: Network Latency
**Test**: Add artificial delay to department service
**Expected**: Retry mechanism attempts 3 times, then fallback

### Scenario 3: Rate Limiting
**Test**: Send 20 requests in 1 second
**Expected**: First 5 succeed, rest rejected with 429 status

### Scenario 4: Distributed Tracing
**Test**: Create student → calls department service
**Expected**: Single trace ID across both services in Zipkin

### Scenario 5: CQRS Event Flow
**Test**: Create enrollment, view events in Axon Server
**Expected**: Command creates event, projection updates read model

## 📈 Load Balancing

Spring Cloud LoadBalancer is automatically enabled via Eureka.

**Test with multiple instances**:
```bash
# Start 2 instances of student-service
java -jar student-service.jar --server.port=8089
java -jar student-service.jar --server.port=8091

# Requests through gateway will be load-balanced
for i in {1..10}; do
  curl http://localhost:8080/students/getAllStudents
done

# Check logs - requests distributed across instances
```

## 🗂️ Project Structure

```
student-management/
├── discovery-service/          # Eureka Server (Service Discovery)
├── config-server/             # Config Server (Centralized Configuration)
├── api-gateway/               # API Gateway (Routing & Load Balancing)
├── student-service/           # Student Microservice (with Resilience4j)
├── department-service/        # Department Microservice
├── enrollment-service/        # Enrollment Service (CQRS + Axon)
├── config-repo/               # Git repository for configurations
├── monitoring/                # Prometheus & Grafana configs
├── docker-compose.yml         # Complete infrastructure setup
└── README.md                  # This file
```

## 🔑 Key Technologies

| Technology | Purpose | Port |
|------------|---------|------|
| Spring Boot 3.5.5 | Application framework | - |
| Spring Cloud 2025.0.0 | Microservices patterns | - |
| Eureka | Service discovery | 8761 |
| Spring Cloud Config | Configuration management | 8888 |
| Spring Cloud Gateway | API Gateway | 8080 |
| OpenFeign | Declarative REST client | - |
| Resilience4j | Resilience patterns | - |
| Axon Framework 4.10.4 | CQRS & Event Sourcing | - |
| MySQL 8.0 | Databases | 3307-3309 |
| Zipkin | Distributed tracing | 9411 |
| Prometheus | Metrics collection | 9090 |
| Grafana | Metrics visualization | 3000 |

## 📝 Evaluation Checklist

### ✅ Architecture Microservices
- [x] Config Server (Centralized configuration)
- [x] Eureka Server (Service discovery)
- [x] Microservices (Student, Department, Enrollment)
- [x] API Gateway (Routing & security)
- [x] Feign Client (Inter-service communication)
- [x] Separate databases per service

### ✅ Résilience (Resilience4j)
- [x] Circuit Breaker (50% failure threshold)
- [x] Retry (3 attempts, exponential backoff)
- [x] Rate Limiter (5 requests/second)
- [x] Fallback methods implemented
- [x] All 5 failure types handled

### ✅ Surveillance (Monitoring)
- [x] Micrometer metrics
- [x] Prometheus integration
- [x] Grafana visualization
- [x] Zipkin distributed tracing
- [x] Health endpoints

### ✅ Load Balancing
- [x] Spring Cloud LoadBalancer
- [x] Eureka-based discovery
- [x] Round-robin distribution

### ✅ CQRS & Axon
- [x] Commands (CreateEnrollment, UpdateStatus)
- [x] Events (EnrollmentCreated, StatusUpdated)
- [x] Aggregate (EnrollmentAggregate)
- [x] Projections (@QueryHandler)
- [x] REST APIs for Commands & Queries
- [x] Axon Server deployment

## 🎯 Devoir Surveillé - Implementation Summary

This implementation covers all requirements from the DS:

1. **Architecture Microservices**: Complete with all components
2. **Communication**: REST + Feign, separate databases
3. **Résilience**: All Resilience4j patterns with fallbacks
4. **Surveillance**: Full monitoring stack (Prometheus + Grafana + Zipkin)
5. **Load Balancing**: Spring Cloud LoadBalancer with Eureka
6. **CQRS**: Full implementation with Axon Framework

## 🤝 Support & Resources

- **Swagger API Docs**: 
  - Student: http://localhost:8089/swagger-ui.html
  - Department: http://localhost:8088/swagger-ui.html
  - Enrollment: http://localhost:8090/swagger-ui.html

- **Health Checks**:
  - Eureka: http://localhost:8761/actuator/health
  - Gateway: http://localhost:8080/actuator/health
  - Student: http://localhost:8089/actuator/health

## 👨‍🎓 Équipe & Module

- **Matière**: Architecture des Composants
- **Section**: GL5
- **Enseignant**: Heithem Abbes
- **Enseignante TPs**: Thouraya LOUATI
- **Faculté**: FST
- **A.U**: 2024-2025

---

**Happy Coding! 🚀**
