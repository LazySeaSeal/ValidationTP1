# 🎓 Student Management - Microservices Architecture

A complete microservices-based student management system built with **Spring Boot 3**, **Spring Cloud**, and modern DevOps practices.

## 📋 Project Overview

This project demonstrates a production-ready microservices architecture implementing:
- Service Discovery (Eureka)
- Centralized Configuration (Spring Cloud Config)
- API Gateway (Spring Cloud Gateway)
- Inter-service Communication (OpenFeign)
- Resilience Patterns (Resilience4j)
- Distributed Tracing (Zipkin)
- Monitoring (Prometheus + Grafana)
- CQRS Pattern (Axon Framework)
- Event Sourcing
- Load Balancing

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        API Gateway (8080)                    │
│              ┌──────────────────────────────────┐            │
│              │  Spring Cloud Gateway + Zipkin   │            │
│              └──────────────────────────────────┘            │
└──────────────────────────┬──────────────────────────────────┘
                           │
         ┌─────────────────┼─────────────────┐
         │                 │                 │
    ┌────▼────┐      ┌─────▼──────┐   ┌─────▼──────┐
    │ Student │      │ Department │   │ Enrollment │
    │ Service │◄────►│  Service   │   │  Service   │
    │  (8089) │      │   (8088)   │   │  (8090)    │
    │         │      │            │   │  + Axon    │
    └────┬────┘      └─────┬──────┘   └─────┬──────┘
         │                 │                 │
    ┌────▼────┐      ┌─────▼──────┐   ┌─────▼──────┐
    │ MySQL   │      │   MySQL    │   │   MySQL    │
    │studentdb│      │departmentdb│   │enrollmentdb│
    └─────────┘      └────────────┘   └────────────┘

         ┌──────────────────────────────────┐
         │   Eureka Discovery (8761)        │
         │   Config Server (8888)           │
         │   Zipkin (9411)                  │
         │   Prometheus (9090)              │
         │   Grafana (3000)                 │
         │   Axon Server (8024, 8124)       │
         └──────────────────────────────────┘
```

## 🚀 Services

| Service | Port | Description | Features |
|---------|------|-------------|----------|
| **Eureka Server** | 8761 | Service Discovery | Service registration & discovery |
| **Config Server** | 8888 | Configuration Management | Centralized config with Git backend |
| **API Gateway** | 8080 | API Gateway & Routing | Load balancing, routing, tracing |
| **Student Service** | 8089 | Student Management | CRUD + Resilience4j patterns |
| **Department Service** | 8088 | Department Management | CRUD operations |
| **Enrollment Service** | 8090 | Enrollment Management | CQRS + Event Sourcing with Axon |

## 🛠️ Tech Stack

- **Java**: 17
- **Spring Boot**: 3.5.5
- **Spring Cloud**: 2025.0.0
- **Database**: MySQL 8
- **Build Tool**: Maven
- **Resilience**: Resilience4j
- **Tracing**: Zipkin + Micrometer
- **Monitoring**: Prometheus + Grafana
- **CQRS/ES**: Axon Framework 4.10.3
- **Containerization**: Docker
- **Orchestration**: Kubernetes

## 📦 Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8+
- Docker & Docker Compose (optional)
- Git

## 🔧 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/student-microservices-architecture.git
cd student-microservices-architecture
```

### 2. Setup MySQL Databases

```sql
CREATE DATABASE studentdb;
CREATE DATABASE departmentdb;
CREATE DATABASE enrollmentdb;
```

Update database credentials in `config-repo/*.yml` files.

### 3. Start Services (Manual)

```bash
# 1. Start Eureka Discovery Service
cd discovery-service
mvn spring-boot:run

# 2. Start Config Server
cd config-server
mvn spring-boot:run

# 3. Start Microservices
cd student-service && mvn spring-boot:run
cd department-service && mvn spring-boot:run
cd enrollment-service && mvn spring-boot:run

# 4. Start API Gateway
cd api-gateway
mvn spring-boot:run
```

### 4. Start with Docker Compose

```bash
docker-compose up -d
```

## 🔍 Accessing Services

| Service | URL | Credentials |
|---------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | - |
| API Gateway | http://localhost:8080 | - |
| Student Service | http://localhost:8080/students | - |
| Department Service | http://localhost:8080/departments | - |
| Swagger UI (Student) | http://localhost:8089/swagger-ui.html | - |
| Prometheus | http://localhost:9090 | - |
| Grafana | http://localhost:3000 | admin/admin |
| Zipkin | http://localhost:9411 | - |
| Axon Server | http://localhost:8024 | - |

## 📝 API Examples

### Student Service

```bash
# Get all students
curl http://localhost:8080/students/getAllStudents

# Get student by ID
curl http://localhost:8080/students/getStudent/1

# Create student
curl -X POST http://localhost:8080/students/addStudent \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "123456789",
    "dateOfBirth": "2000-01-01",
    "address": "123 Main St",
    "departmentId": 1
  }'
```

### Department Service

```bash
# Get all departments
curl http://localhost:8080/departments/getAllDepartments

# Create department
curl -X POST http://localhost:8080/departments/addDepartment \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Computer Science",
    "location": "Building A",
    "phone": "987654321",
    "head": "Dr. Smith"
  }'
```

### Enrollment Service (CQRS)

```bash
# Create enrollment (Command)
curl -X POST http://localhost:8090/enrollments/create \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "courseId": "CS101",
    "semester": "Spring 2026"
  }'

# Get all enrollments (Query)
curl http://localhost:8090/enrollments/all

# Get enrollment by ID (Query)
curl http://localhost:8090/enrollments/647e8a9f-1234-5678-90ab-cdef12345678
```

## 🛡️ Resilience Patterns

### Circuit Breaker
- **Sliding Window Size**: 10 requests
- **Failure Rate Threshold**: 50%
- **Wait Duration**: 10 seconds
- **Fallback**: Returns cached or default data

### Retry
- **Max Attempts**: 3
- **Wait Duration**: 500ms
- **Exponential Backoff**: 2x multiplier

### Rate Limiter
- **Limit**: 5 requests per second
- **Timeout**: Immediate failure

## 📊 Monitoring & Observability

### Prometheus Metrics
- Access: http://localhost:9090
- Scrapes metrics from all services every 15s
- Custom metrics for business logic

### Grafana Dashboards
- Access: http://localhost:3000 (admin/admin)
- Pre-configured dashboards for:
  - JVM metrics
  - Spring Boot metrics
  - Resilience4j metrics
  - Custom business metrics

### Zipkin Tracing
- Access: http://localhost:9411
- Distributed tracing across all services
- Request flow visualization
- Performance bottleneck identification

## 🎯 CQRS & Event Sourcing

The Enrollment Service demonstrates CQRS pattern using Axon Framework:

### Commands
- `CreateEnrollmentCommand`: Create new enrollment
- `UpdateEnrollmentCommand`: Update enrollment status
- `DeleteEnrollmentCommand`: Cancel enrollment

### Events
- `EnrollmentCreatedEvent`: Fired when enrollment is created
- `EnrollmentUpdatedEvent`: Fired when enrollment is updated
- `EnrollmentDeletedEvent`: Fired when enrollment is deleted

### Query Model
- Separate read model optimized for queries
- Event handlers update query database
- Eventual consistency

## 🐳 Docker Deployment

```bash
# Build all services
mvn clean package -DskipTests

# Build Docker images
docker-compose build

# Start all services
docker-compose up -d

# Check logs
docker-compose logs -f

# Stop all services
docker-compose down
```

## ☸️ Kubernetes Deployment

```bash
# Apply all configurations
kubectl apply -f k8s/

# Check deployments
kubectl get deployments

# Check services
kubectl get services

# Check pods
kubectl get pods
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific service tests
cd student-service && mvn test

# Integration tests
mvn verify
```

## 📚 Project Structure

```
student-microservices-architecture/
├── discovery-service/          # Eureka Server
├── config-server/              # Spring Cloud Config
├── api-gateway/                # API Gateway
├── student-service/            # Student microservice
├── department-service/         # Department microservice
├── enrollment-service/         # Enrollment CQRS service
├── config-repo/                # Configuration files
├── k8s/                        # Kubernetes manifests
├── docker-compose.yml          # Docker Compose
├── monitoring/                 # Prometheus & Grafana configs
└── README.md
```

## 🔐 Security Considerations

⚠️ **Important**: This is a demonstration project. For production:
- Add Spring Security
- Implement JWT authentication
- Use OAuth2/OpenID Connect
- Encrypt sensitive configuration
- Use secrets management (Vault, K8s Secrets)
- Enable HTTPS/TLS
- Implement rate limiting
- Add API key validation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- Your Name - [GitHub Profile](https://github.com/YOUR_USERNAME)

## 🙏 Acknowledgments

- Spring Cloud Team
- Axon Framework
- Resilience4j Project
- OpenFeign Team

## 📞 Support

For support, email your.email@example.com or open an issue.

---

**Happy Coding! 🚀**
