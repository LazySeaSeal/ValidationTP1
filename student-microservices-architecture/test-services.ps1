# Test Script for Student Management Microservices
# This script tests all endpoints and verifies the microservices architecture

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  Microservices Health Check" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

$services = @(
    @{Name="Eureka Server"; Url="http://localhost:8761/actuator/health"},
    @{Name="Config Server"; Url="http://localhost:8888/actuator/health"},
    @{Name="API Gateway"; Url="http://localhost:8080/actuator/health"},
    @{Name="Student Service"; Url="http://localhost:8089/actuator/health"},
    @{Name="Department Service"; Url="http://localhost:8088/actuator/health"},
    @{Name="Enrollment Service"; Url="http://localhost:8090/actuator/health"}
)

Write-Host "Checking Services Health..." -ForegroundColor Yellow
Write-Host ""

foreach ($service in $services) {
    try {
        $response = Invoke-WebRequest -Uri $service.Url -Method Get -TimeoutSec 5 -UseBasicParsing
        if ($response.StatusCode -eq 200) {
            Write-Host "✓ $($service.Name) is UP" -ForegroundColor Green
        }
    } catch {
        Write-Host "✗ $($service.Name) is DOWN" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  Testing API Endpoints" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Test 1: Create Department
Write-Host "Test 1: Creating Department..." -ForegroundColor Yellow
$departmentBody = @{
    name = "Computer Science"
    location = "Building A"
    phone = "123-456-7890"
    head = "Dr. John Smith"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/departments/addDepartment" `
        -Method Post `
        -Body $departmentBody `
        -ContentType "application/json" `
        -TimeoutSec 10
    Write-Host "✓ Department Created: $($response.name)" -ForegroundColor Green
    $departmentId = $response.idDepartment
    Write-Host "  Department ID: $departmentId" -ForegroundColor Gray
} catch {
    Write-Host "✗ Failed to create department: $($_.Exception.Message)" -ForegroundColor Red
    $departmentId = 1
}

Start-Sleep -Seconds 2

# Test 2: Get All Departments
Write-Host ""
Write-Host "Test 2: Getting All Departments..." -ForegroundColor Yellow
try {
    $departments = Invoke-RestMethod -Uri "http://localhost:8080/departments/getAllDepartments" `
        -Method Get `
        -TimeoutSec 10
    Write-Host "✓ Retrieved $($departments.Count) department(s)" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to get departments: $($_.Exception.Message)" -ForegroundColor Red
}

Start-Sleep -Seconds 2

# Test 3: Create Student
Write-Host ""
Write-Host "Test 3: Creating Student..." -ForegroundColor Yellow
$studentBody = @{
    firstName = "Alice"
    lastName = "Johnson"
    email = "alice.johnson@university.edu"
    phone = "555-1234"
    dateOfBirth = "2000-05-15"
    address = "123 University Ave"
    departmentId = $departmentId
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/students/addStudent" `
        -Method Post `
        -Body $studentBody `
        -ContentType "application/json" `
        -TimeoutSec 10
    Write-Host "✓ Student Created: $($response.firstName) $($response.lastName)" -ForegroundColor Green
    $studentId = $response.idStudent
    Write-Host "  Student ID: $studentId" -ForegroundColor Gray
} catch {
    Write-Host "✗ Failed to create student: $($_.Exception.Message)" -ForegroundColor Red
    $studentId = 1
}

Start-Sleep -Seconds 2

# Test 4: Get All Students
Write-Host ""
Write-Host "Test 4: Getting All Students..." -ForegroundColor Yellow
try {
    $students = Invoke-RestMethod -Uri "http://localhost:8080/students/getAllStudents" `
        -Method Get `
        -TimeoutSec 10
    Write-Host "✓ Retrieved $($students.Count) student(s)" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to get students: $($_.Exception.Message)" -ForegroundColor Red
}

Start-Sleep -Seconds 2

# Test 5: Create Enrollment (CQRS Command)
Write-Host ""
Write-Host "Test 5: Creating Enrollment (CQRS)..." -ForegroundColor Yellow
$enrollmentBody = @{
    studentId = $studentId
    courseId = "CS101"
    semester = "Spring 2026"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/enrollments/create" `
        -Method Post `
        -Body $enrollmentBody `
        -ContentType "application/json" `
        -TimeoutSec 10
    Write-Host "✓ Enrollment Created" -ForegroundColor Green
    Write-Host "  Enrollment ID: $($response.enrollmentId)" -ForegroundColor Gray
    $enrollmentId = $response.enrollmentId
} catch {
    Write-Host "✗ Failed to create enrollment: $($_.Exception.Message)" -ForegroundColor Red
}

Start-Sleep -Seconds 2

# Test 6: Query Enrollments (CQRS Query)
Write-Host ""
Write-Host "Test 6: Querying Enrollments (CQRS)..." -ForegroundColor Yellow
try {
    $enrollments = Invoke-RestMethod -Uri "http://localhost:8080/enrollments/all" `
        -Method Get `
        -TimeoutSec 10
    Write-Host "✓ Retrieved $($enrollments.Count) enrollment(s)" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to query enrollments: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Circuit Breaker Test
Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  Testing Resilience Patterns" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Test 7: Circuit Breaker Health..." -ForegroundColor Yellow
try {
    $cbHealth = Invoke-RestMethod -Uri "http://localhost:8089/actuator/health/circuitBreakers" `
        -Method Get `
        -TimeoutSec 10
    Write-Host "✓ Circuit Breaker Status: $($cbHealth.status)" -ForegroundColor Green
} catch {
    Write-Host "⚠ Circuit Breaker health check not available" -ForegroundColor Yellow
}

# Test 8: Metrics
Write-Host ""
Write-Host "Test 8: Checking Prometheus Metrics..." -ForegroundColor Yellow
try {
    $metrics = Invoke-RestMethod -Uri "http://localhost:8089/actuator/prometheus" `
        -Method Get `
        -TimeoutSec 10
    Write-Host "✓ Metrics available ($(($metrics -split "`n").Count) lines)" -ForegroundColor Green
} catch {
    Write-Host "✗ Metrics not available: $($_.Exception.Message)" -ForegroundColor Red
}

# Summary
Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  Test Summary" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Key URLs:" -ForegroundColor Yellow
Write-Host "  • Eureka Dashboard: http://localhost:8761" -ForegroundColor Gray
Write-Host "  • API Gateway: http://localhost:8080" -ForegroundColor Gray
Write-Host "  • Zipkin Tracing: http://localhost:9411" -ForegroundColor Gray
Write-Host "  • Prometheus: http://localhost:9090" -ForegroundColor Gray
Write-Host "  • Grafana: http://localhost:3000 (admin/admin)" -ForegroundColor Gray
Write-Host "  • Axon Server: http://localhost:8024" -ForegroundColor Gray
Write-Host ""
Write-Host "Swagger URLs:" -ForegroundColor Yellow
Write-Host "  • Student Service: http://localhost:8089/swagger-ui.html" -ForegroundColor Gray
Write-Host "  • Department Service: http://localhost:8088/swagger-ui.html" -ForegroundColor Gray
Write-Host "  • Enrollment Service: http://localhost:8090/swagger-ui.html" -ForegroundColor Gray
Write-Host ""
Write-Host "Tests completed!" -ForegroundColor Green
