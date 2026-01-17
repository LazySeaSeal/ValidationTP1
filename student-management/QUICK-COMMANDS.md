# 🚀 Quick Commands Reference

## 🏗️ Build Commands

```powershell
# Build all services at once
.\setup.ps1

# Build individual service
cd <service-name>
mvn clean package -DskipTests
cd ..
```

## 🐳 Docker Commands

```powershell
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f [service-name]

# Restart specific service
docker-compose restart student-service

# Check status
docker-compose ps

# Remove volumes (clean slate)
docker-compose down -v
```

## ▶️ Run Services Locally

```powershell
# Eureka Server
cd discovery-service && mvn spring-boot:run

# Config Server
cd config-server && mvn spring-boot:run

# Student Service
cd student-service && mvn spring-boot:run

# Department Service
cd department-service && mvn spring-boot:run

# Enrollment Service
cd enrollment-service && mvn spring-boot:run

# API Gateway
cd api-gateway && mvn spring-boot:run
```

## 🧪 Testing Commands

```powershell
# Run automated tests
.\test-services.ps1

# Test specific endpoint
curl http://localhost:8080/students/getAllStudents

# Test with JSON body
curl -X POST http://localhost:8080/departments/addDepartment `
  -H "Content-Type: application/json" `
  -d '{\"name\":\"CS\",\"location\":\"Building A\",\"phone\":\"123\",\"head\":\"Dr. Smith\"}'
```

## 📊 Monitoring Commands

```powershell
# Check health
curl http://localhost:8089/actuator/health

# Get metrics
curl http://localhost:8089/actuator/prometheus

# Get circuit breaker state
curl http://localhost:8089/actuator/health | jq .components.circuitBreakers

# Refresh config without restart
curl -X POST http://localhost:8089/actuator/refresh
```

## 🔍 Debugging Commands

```powershell
# View Eureka registered services
curl http://localhost:8761/eureka/apps | Select-String "instance"

# Check service discovery
curl http://localhost:8761/eureka/apps/STUDENT-MICROSERVICE-APP

# Test rate limiter (run multiple times)
1..10 | ForEach-Object { curl http://localhost:8080/students/getAllStudents }

# Check Axon events
# Open browser: http://localhost:8024
```

## 🗄️ Database Commands

```powershell
# Connect to MySQL (Docker)
docker exec -it mysql-student mysql -uroot -proot studentdb
docker exec -it mysql-department mysql -uroot -proot departmentdb
docker exec -it mysql-enrollment mysql -uroot -proot enrollmentdb

# Show tables
SHOW TABLES;

# Query data
SELECT * FROM student;
SELECT * FROM department;
SELECT * FROM enrollment_query;
```

## 🔧 Infrastructure Services

```powershell
# Start Axon Server (standalone)
docker run -d --name axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver

# Start Zipkin (standalone)
docker run -d --name zipkin -p 9411:9411 openzipkin/zipkin

# Start Prometheus (standalone)
docker run -d --name prometheus -p 9090:9090 `
  -v ${PWD}/monitoring/prometheus.yml:/etc/prometheus/prometheus.yml `
  prom/prometheus

# Start Grafana (standalone)
docker run -d --name grafana -p 3000:3000 `
  -e GF_SECURITY_ADMIN_PASSWORD=admin `
  grafana/grafana
```

## 🌐 Access URLs

```powershell
# Open in browser
Start-Process "http://localhost:8761"  # Eureka
Start-Process "http://localhost:8080"  # Gateway
Start-Process "http://localhost:9411"  # Zipkin
Start-Process "http://localhost:9090"  # Prometheus
Start-Process "http://localhost:3000"  # Grafana
Start-Process "http://localhost:8024"  # Axon Server
```

## 📝 Git Commands

```powershell
# Check current branch
git branch

# Switch to microservices branch
git checkout microservices

# View changes
git status

# Commit changes
git add .
git commit -m "Added microservices architecture"

# Push to remote
git push origin microservices
```

## 🧹 Cleanup Commands

```powershell
# Stop and remove all containers
docker-compose down

# Remove all volumes (delete data)
docker-compose down -v

# Remove images
docker rmi $(docker images -q student-management*)

# Clean Maven
mvn clean

# Remove target folders
Get-ChildItem -Recurse -Directory -Filter target | Remove-Item -Recurse -Force
```

## 🎯 Quick Test Scenarios

### Test 1: Normal Flow
```powershell
# Create department
$dept = Invoke-RestMethod -Uri "http://localhost:8080/departments/addDepartment" `
  -Method Post -ContentType "application/json" `
  -Body '{"name":"CS","location":"A","phone":"123","head":"Dr. Smith"}'

# Create student
Invoke-RestMethod -Uri "http://localhost:8080/students/addStudent" `
  -Method Post -ContentType "application/json" `
  -Body "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\",\"departmentId\":$($dept.idDepartment)}"
```

### Test 2: Circuit Breaker
```powershell
# Stop department service
docker stop department-service

# Try to create student (should use fallback)
Invoke-RestMethod -Uri "http://localhost:8080/students/addStudent" `
  -Method Post -ContentType "application/json" `
  -Body '{"firstName":"Jane","lastName":"Doe","email":"jane@example.com","departmentId":999}'
```

### Test 3: Rate Limiter
```powershell
# Send 10 requests rapidly
1..10 | ForEach-Object { 
  try { 
    Invoke-RestMethod "http://localhost:8080/students/getAllStudents" 
    Write-Host "Request $_ : Success" -ForegroundColor Green
  } catch { 
    Write-Host "Request $_ : Rate Limited" -ForegroundColor Red
  }
}
```

### Test 4: CQRS Flow
```powershell
# Create enrollment
$enrollmentId = Invoke-RestMethod -Uri "http://localhost:8090/enrollments" `
  -Method Post -ContentType "application/json" `
  -Body '{"studentId":1,"courseId":101}'

# Query enrollment
Start-Sleep -Seconds 2
Invoke-RestMethod -Uri "http://localhost:8090/enrollments/$enrollmentId"

# Update status
Invoke-RestMethod -Uri "http://localhost:8090/enrollments/$enrollmentId/status" `
  -Method Put -ContentType "application/json" `
  -Body '{"status":"APPROVED"}'
```

## 📦 Export/Import

```powershell
# Export Docker images
docker save -o student-management-images.tar $(docker images -q student-management*)

# Import Docker images
docker load -i student-management-images.tar

# Export Postman collection
# File already created: Postman-Collection.json
# Import in Postman: File > Import > Choose file
```

## 🔄 Restart Services

```powershell
# Restart all
docker-compose restart

# Restart specific service
docker-compose restart student-service

# Restart with rebuild
docker-compose up -d --build student-service
```

## 📈 Performance Testing

```powershell
# Stress test (requires Apache Bench)
ab -n 1000 -c 10 http://localhost:8080/students/getAllStudents

# Or use PowerShell loop
1..100 | ForEach-Object -Parallel { 
  Invoke-RestMethod http://localhost:8080/students/getAllStudents 
} -ThrottleLimit 20
```

## 🆘 Troubleshooting

```powershell
# Check port usage
netstat -ano | findstr :8080
netstat -ano | findstr :8761

# Kill process on port
Stop-Process -Id <PID> -Force

# Check container logs
docker logs student-service --tail 100 -f

# Check container shell
docker exec -it student-service sh

# Restart stuck service
docker-compose restart student-service

# Check network connectivity
docker network ls
docker network inspect student-management_microservices-network
```

---

**💡 Tip**: Import `Postman-Collection.json` for easier API testing!

**📚 Full Documentation**: See [MICROSERVICES-README.md](MICROSERVICES-README.md)
