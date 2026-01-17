# 🎓 Student Management Microservices

A complete microservices architecture implementation for student management using Spring Cloud, demonstrating service discovery, API Gateway, circuit breakers, CQRS, event sourcing, and distributed tracing.

## 📋 Architecture Overview

This project implements a microservices architecture with the following components:

- **Eureka Discovery Service** (Port 8761) - Service registry
- **Config Server** (Port 8888) - Centralized configuration management
- **API Gateway** (Port 8080) - Single entry point for all services
- **Student Service** (Port 8089) - Student data management with Resilience4j patterns
- **Department Service** (Port 8088) - Department management
- **Enrollment Service** (Port 8090) - CQRS with Axon Framework & Event Sourcing
- **MySQL Databases** (Ports 3307, 3308, 3309) - One database per service
- **Axon Server** (Ports 8024, 8124) - Event store for CQRS
- **Zipkin** (Port 9411) - Distributed tracing
- **Prometheus** (Port 9090) - Metrics collection
- **Grafana** (Port 3000) - Metrics visualization (admin/admin)

## 🚀 Quick Start on a New PC

### Prerequisites

Before starting, ensure you have the following installed:

1. **Java 17** or higher
   - Download from: https://adoptium.net/
   - Verify: `java -version`

2. **Maven 3.8+**
   - Download from: https://maven.apache.org/download.cgi
   - Verify: `mvn -version`

3. **Docker Desktop**
   - Download from: https://www.docker.com/products/docker-desktop
   - Verify: `docker --version` and `docker-compose --version`
   - **IMPORTANT**: Make sure Docker Desktop is running before proceeding!

4. **Git**
   - Download from: https://git-scm.com/downloads
   - Verify: `git --version`

### Step 1: Clone the Repository

```bash
git clone https://github.com/LazySeaSeal/ValidationTP1.git
cd ValidationTP1/student-management
```

### Step 2: Build All Microservices

Run the following commands to build all services:

```bash
# Build Discovery Service
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

**OR** use the PowerShell setup script (Windows):
```powershell
.\setup.ps1
```

### Step 3: Start All Services with Docker Compose

```bash
docker-compose up -d
```

This will:
- Pull all required Docker images (MySQL, Prometheus, Grafana, Zipkin, Axon Server)
- Build Docker images for all microservices
- Start all containers in detached mode

### Step 4: Wait for Services to Initialize

Services take about 30-60 seconds to fully start. You can monitor the startup:

```bash
# Check all containers status
docker-compose ps

# View logs for a specific service
docker-compose logs -f student-service
```

### Step 5: Verify Everything is Running

Open your browser and check:

- **Eureka Dashboard**: http://localhost:8761
  - All services should be registered here
  
- **API Gateway**: http://localhost:8080
  - Main entry point for all API calls

## 🔍 Service URLs

| Service | URL | Description |
|---------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | Service registry UI |
| API Gateway | http://localhost:8080 | Main API entry point |
| Student Service | http://localhost:8089 | Direct access (use Gateway instead) |
| Department Service | http://localhost:8088 | Direct access (use Gateway instead) |
| Enrollment Service | http://localhost:8090 | CQRS service |
| Config Server | http://localhost:8888 | Configuration management |
| Prometheus | http://localhost:9090 | Metrics collection |
| Grafana | http://localhost:3000 | Metrics visualization (admin/admin) |
| Zipkin | http://localhost:9411 | Distributed tracing |
| Axon Server | http://localhost:8024 | Event store dashboard |

## 🧪 Testing the Services

### Option 1: Use the Test Script (PowerShell)

```powershell
.\test-services.ps1
```

### Option 2: Import Postman Collection

Import the `Postman-Collection.json` file into Postman to test all endpoints.

### Option 3: Manual Testing with cURL

**Create a Department:**
```bash
curl -X POST http://localhost:8080/departments \
  -H "Content-Type: application/json" \
  -d '{"name": "Computer Science", "location": "Building A", "phone": "123-456-7890", "head": "Dr. Smith"}'
```

**Create a Student:**
```bash
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"firstName": "John", "lastName": "Doe", "email": "john.doe@example.com", "departmentId": 1}'
```

**Get All Students:**
```bash
curl http://localhost:8080/students
```

## 🛠️ Useful Docker Commands

```bash
# View all containers
docker-compose ps

# View logs
docker-compose logs -f [service-name]

# Restart a specific service
docker-compose restart student-service

# Stop all services
docker-compose down

# Stop and remove all data (clean slate)
docker-compose down -v

# Rebuild and restart all services
docker-compose up -d --build

# Rebuild a specific service
docker-compose up -d --build student-service
```

## 🏗️ Resilience Patterns Implemented

All configured in `config-repo/student-microservice-app.yml`:

### Circuit Breaker
- Prevents cascade failures
- Opens after 50% failure rate
- Automatic recovery testing

### Retry
- 3 retry attempts
- Exponential backoff (500ms base delay)
- Protects against transient failures

### Rate Limiter
- 5 requests per second limit
- Prevents service overload

## 📊 Monitoring

### Prometheus Metrics
Visit http://localhost:9090 to query metrics:
- `http_server_requests_seconds`
- `resilience4j_circuitbreaker_state`
- `jvm_memory_used_bytes`

### Grafana Dashboards
1. Open http://localhost:3000
2. Login: admin / admin
3. Pre-configured Prometheus datasource available
4. Create custom dashboards for service monitoring

### Zipkin Tracing
Visit http://localhost:9411 to view distributed traces:
- Track requests across services
- Identify performance bottlenecks
- View service dependencies

## 🔧 Troubleshooting

### Services Not Starting

1. **Check Docker is running:**
   ```bash
   docker ps
   ```

2. **Check service logs:**
   ```bash
   docker-compose logs student-service
   ```

3. **Restart specific service:**
   ```bash
   docker-compose restart student-service
   ```

### Services Not Registering with Eureka

- Wait 30-60 seconds for registration
- Check Eureka dashboard at http://localhost:8761
- Verify network connectivity between containers

### Build Failures

1. **Clean Maven cache:**
   ```bash
   mvn clean
   ```

2. **Update dependencies:**
   ```bash
   mvn clean install -U
   ```

3. **Check Java version:**
   ```bash
   java -version  # Should be Java 17+
   ```

### Database Connection Issues

```bash
# Check MySQL containers are healthy
docker-compose ps

# Check MySQL logs
docker-compose logs mysql-student
docker-compose logs mysql-department
docker-compose logs mysql-enrollment
```

## 📁 Project Structure

```
student-management/
├── api-gateway/              # API Gateway (Spring Cloud Gateway)
├── config-server/            # Centralized configuration
├── discovery-service/        # Eureka Server
├── student-service/          # Student management with Resilience4j
├── department-service/       # Department management
├── enrollment-service/       # CQRS with Axon Framework
├── config-repo/             # External configuration files
├── monitoring/              # Prometheus & Grafana configs
├── docker-compose.yml       # Docker orchestration
├── setup.ps1               # Build script (Windows)
├── test-services.ps1       # Testing script (Windows)
└── Postman-Collection.json # API testing collection
```

## 🎯 Key Features Demonstrated

- ✅ Service Discovery with Eureka
- ✅ Centralized Configuration with Config Server
- ✅ API Gateway Pattern
- ✅ Circuit Breaker, Retry, Rate Limiting (Resilience4j)
- ✅ CQRS & Event Sourcing (Axon Framework)
- ✅ Distributed Tracing (Zipkin)
- ✅ Metrics Collection (Prometheus)
- ✅ Metrics Visualization (Grafana)
- ✅ Database per Service Pattern
- ✅ Containerization (Docker)
- ✅ Inter-Service Communication (OpenFeign)

## 🤝 Contributing

This is an educational project demonstrating microservices patterns. Feel free to fork and experiment!

## 📚 Additional Documentation

- [MICROSERVICES-README.md](MICROSERVICES-README.md) - Detailed architecture documentation
- [QUICK-COMMANDS.md](QUICK-COMMANDS.md) - Command reference
- [EVALUATION-CHECKLIST.md](EVALUATION-CHECKLIST.md) - Project requirements checklist
- [MIGRATION-SUMMARY.md](MIGRATION-SUMMARY.md) - Migration details

## 📝 License

This project is for educational purposes.

---

**Built with ❤️ for learning microservices architecture**
