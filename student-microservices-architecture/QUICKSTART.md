# 🚀 Quick Start Guide

## Prerequisites Check

```bash
# Check Java version
java -version  # Should be 17+

# Check Maven version
mvn -version   # Should be 3.8+

# Check Docker
docker --version
docker-compose --version

# Check MySQL
mysql --version
```

## Setup Steps

### Option 1: Local Development (Without Docker)

#### 1. Setup Databases

```sql
-- Connect to MySQL
mysql -u root -p

-- Create databases
CREATE DATABASE studentdb;
CREATE DATABASE departmentdb;
CREATE DATABASE enrollmentdb;

-- (Optional) Create user
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON studentdb.* TO 'appuser'@'localhost';
GRANT ALL PRIVILEGES ON departmentdb.* TO 'appuser'@'localhost';
GRANT ALL PRIVILEGES ON enrollmentdb.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;
```

#### 2. Update Configuration

Edit files in `config-repo/`:
- `student-microservice-app.yml`
- `department-microservice-app.yml`
- `enrollment-microservice-app.yml`

Update database credentials:
```yaml
spring:
  datasource:
    username: root  # or your username
    password: yourpassword
```

#### 3. Start Services in Order

```bash
# Terminal 1: Start Eureka Discovery Service
cd discovery-service
mvn clean install
mvn spring-boot:run

# Terminal 2: Start Config Server
cd config-server
mvn clean install
mvn spring-boot:run

# Terminal 3: Start Department Service
cd department-service
mvn clean install
mvn spring-boot:run

# Terminal 4: Start Student Service
cd student-service
mvn clean install
mvn spring-boot:run

# Terminal 5: Start Enrollment Service (CQRS)
cd enrollment-service
mvn clean install
mvn spring-boot:run

# Terminal 6: Start API Gateway
cd api-gateway
mvn clean install
mvn spring-boot:run
```

#### 4. Start Monitoring (Optional)

```bash
# Start Zipkin
docker run -d -p 9411:9411 openzipkin/zipkin

# Start Axon Server (for Enrollment Service)
docker run -d -p 8024:8024 -p 8124:8124 --name axon-server axoniq/axonserver

# Start Prometheus
docker run -d -p 9090:9090 -v ./monitoring/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus

# Start Grafana
docker run -d -p 3000:3000 grafana/grafana
```

### Option 2: Docker Compose (Recommended)

```bash
# Build and start all services
docker-compose up --build -d

# Check logs
docker-compose logs -f

# Check specific service
docker-compose logs -f student-service

# Stop all services
docker-compose down

# Remove volumes (clean start)
docker-compose down -v
```

## Verification

### 1. Check Eureka Dashboard
```
http://localhost:8761
```
You should see all services registered:
- CONFIG-SERVER
- STUDENT-MICROSERVICE-APP
- DEPARTMENT-MICROSERVICE-APP
- ENROLLMENT-MICROSERVICE-APP
- API-GATEWAY

### 2. Test API Gateway
```bash
# Health check
curl http://localhost:8080/actuator/health

# Test routing
curl http://localhost:8080/students/getAllStudents
curl http://localhost:8080/departments/getAllDepartments
```

### 3. Check Monitoring

- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)
- **Zipkin**: http://localhost:9411
- **Axon Server**: http://localhost:8024

## Testing the Services

### Create a Department

```bash
curl -X POST http://localhost:8080/departments/addDepartment \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Computer Science",
    "location": "Building A",
    "phone": "123456789",
    "head": "Dr. Smith"
  }'
```

### Create a Student

```bash
curl -X POST http://localhost:8080/students/addStudent \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@university.edu",
    "phone": "987654321",
    "dateOfBirth": "2000-01-15",
    "address": "123 University Ave",
    "departmentId": 1
  }'
```

### Create an Enrollment (CQRS Command)

```bash
curl -X POST http://localhost:8080/enrollments/create \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "courseId": "CS101",
    "semester": "Spring 2026"
  }'
```

### Query Enrollments (CQRS Query)

```bash
# Get all enrollments
curl http://localhost:8080/enrollments/all

# Get specific enrollment
curl http://localhost:8080/enrollments/{enrollment-id}
```

## Testing Resilience Patterns

### Circuit Breaker Test

```bash
# Stop department service
docker-compose stop department-service

# Try to create a student (should trigger circuit breaker)
curl -X POST http://localhost:8080/students/addStudent \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Doe",
    "email": "jane@university.edu",
    "phone": "111222333",
    "dateOfBirth": "2001-03-20",
    "address": "456 Campus Rd",
    "departmentId": 1
  }'

# Start department service again
docker-compose start department-service
```

### Rate Limiter Test

```bash
# Send multiple requests rapidly (> 5 req/s should be rate limited)
for i in {1..10}; do
  curl http://localhost:8080/students/getAllStudents &
done
wait
```

## Swagger Documentation

Access Swagger UI for each service:

- **Student Service**: http://localhost:8089/swagger-ui.html
- **Department Service**: http://localhost:8088/swagger-ui.html
- **Enrollment Service**: http://localhost:8090/swagger-ui.html

## Troubleshooting

### Services not registering with Eureka

1. Check Eureka is running: http://localhost:8761
2. Check service logs: `docker-compose logs service-name`
3. Verify `eureka.client.service-url.defaultZone` in configuration

### Database connection errors

1. Check MySQL is running: `docker-compose ps`
2. Verify database credentials in config files
3. Check database exists: `docker exec -it mysql-student mysql -uroot -proot -e "SHOW DATABASES;"`

### Port conflicts

```bash
# Check if ports are already in use
netstat -ano | findstr :8080
netstat -ano | findstr :8761

# Kill process using port (Windows)
taskkill /PID <PID> /F
```

### Circuit breaker not working

1. Ensure Resilience4j dependency is added
2. Check `@CircuitBreaker` annotation on methods
3. Verify configuration in `config-repo/*.yml`
4. Check actuator endpoint: http://localhost:8089/actuator/circuitbreakers

## Development Tips

### Hot Reload

Use Spring Boot DevTools for hot reload during development:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

### View Config Server Properties

```bash
# View student service config
curl http://localhost:8888/student-microservice-app/default

# View department service config
curl http://localhost:8888/department-microservice-app/default
```

### Clear Axon Event Store

```bash
# Connect to enrollment database
docker exec -it mysql-enrollment mysql -uroot -proot enrollmentdb

# Clear events
DELETE FROM domain_event_entry;
DELETE FROM association_value_entry;
DELETE FROM saga_entry;
```

## Next Steps

1. ✅ All services running
2. ✅ Test CRUD operations
3. ✅ Test resilience patterns
4. ✅ View distributed traces in Zipkin
5. ✅ Check metrics in Prometheus
6. ✅ Create Grafana dashboards
7. ✅ Test CQRS operations
8. 🔒 Add Spring Security
9. 🔑 Implement JWT authentication
10. ☸️ Deploy to Kubernetes

## Support

For issues, check:
- Service logs: `docker-compose logs -f`
- Eureka Dashboard: http://localhost:8761
- Actuator endpoints: http://localhost:PORT/actuator
