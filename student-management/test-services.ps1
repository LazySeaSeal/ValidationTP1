# 🧪 Microservices Testing Script

Write-Host "===================================" -ForegroundColor Cyan
Write-Host "Microservices Testing Suite" -ForegroundColor Cyan
Write-Host "===================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080"

# Test 1: Check services health
Write-Host "Test 1: Checking service health..." -ForegroundColor Yellow
$services = @(
    @{Name="Eureka"; Url="http://localhost:8761/actuator/health"},
    @{Name="Config Server"; Url="http://localhost:8888/actuator/health"},
    @{Name="API Gateway"; Url="http://localhost:8080/actuator/health"},
    @{Name="Student Service"; Url="http://localhost:8089/actuator/health"},
    @{Name="Department Service"; Url="http://localhost:8088/actuator/health"},
    @{Name="Enrollment Service"; Url="http://localhost:8090/actuator/health"}
)

foreach ($service in $services) {
    try {
        $response = Invoke-RestMethod -Uri $service.Url -Method Get -TimeoutSec 5
        if ($response.status -eq "UP") {
            Write-Host "✓ $($service.Name) is UP" -ForegroundColor Green
        }
    } catch {
        Write-Host "✗ $($service.Name) is DOWN" -ForegroundColor Red
    }
}
Write-Host ""

# Test 2: Create a department
Write-Host "Test 2: Creating a department..." -ForegroundColor Yellow
$deptBody = @{
    name = "Computer Science"
    location = "Building A"
    phone = "123456789"
    head = "Dr. Smith"
} | ConvertTo-Json

try {
    $dept = Invoke-RestMethod -Uri "$baseUrl/departments/addDepartment" -Method Post -Body $deptBody -ContentType "application/json"
    Write-Host "✓ Department created with ID: $($dept.idDepartment)" -ForegroundColor Green
    $deptId = $dept.idDepartment
} catch {
    Write-Host "✗ Failed to create department: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 3: Create a student
Write-Host "Test 3: Creating a student..." -ForegroundColor Yellow
$studentBody = @{
    firstName = "John"
    lastName = "Doe"
    email = "john.doe@example.com"
    phone = "987654321"
    departmentId = $deptId
} | ConvertTo-Json

try {
    $student = Invoke-RestMethod -Uri "$baseUrl/students/addStudent" -Method Post -Body $studentBody -ContentType "application/json"
    Write-Host "✓ Student created with ID: $($student.idStudent)" -ForegroundColor Green
    $studentId = $student.idStudent
} catch {
    Write-Host "✗ Failed to create student: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 4: Get all students
Write-Host "Test 4: Fetching all students..." -ForegroundColor Yellow
try {
    $students = Invoke-RestMethod -Uri "$baseUrl/students/getAllStudents" -Method Get
    Write-Host "✓ Found $($students.Count) students" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to fetch students: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 5: Test resilience - Rate Limiter
Write-Host "Test 5: Testing Rate Limiter (sending 10 rapid requests)..." -ForegroundColor Yellow
$successCount = 0
$failCount = 0

for ($i = 1; $i -le 10; $i++) {
    try {
        Invoke-RestMethod -Uri "$baseUrl/students/getAllStudents" -Method Get -ErrorAction Stop | Out-Null
        $successCount++
    } catch {
        $failCount++
    }
}
Write-Host "  Success: $successCount | Rate Limited: $failCount" -ForegroundColor Cyan
if ($failCount -gt 0) {
    Write-Host "✓ Rate limiter working correctly" -ForegroundColor Green
}
Write-Host ""

# Test 6: Create enrollment (CQRS)
Write-Host "Test 6: Creating enrollment (CQRS)..." -ForegroundColor Yellow
$enrollmentBody = @{
    studentId = $studentId
    courseId = 101
} | ConvertTo-Json

try {
    $enrollmentId = Invoke-RestMethod -Uri "http://localhost:8090/enrollments" -Method Post -Body $enrollmentBody -ContentType "application/json"
    Write-Host "✓ Enrollment created with ID: $enrollmentId" -ForegroundColor Green
    
    # Query the enrollment
    Start-Sleep -Seconds 2
    $enrollment = Invoke-RestMethod -Uri "http://localhost:8090/enrollments/$enrollmentId" -Method Get
    Write-Host "✓ Enrollment queried: Status = $($enrollment.status)" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to create/query enrollment: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 7: Check monitoring endpoints
Write-Host "Test 7: Checking monitoring endpoints..." -ForegroundColor Yellow
try {
    $metrics = Invoke-RestMethod -Uri "http://localhost:8089/actuator/prometheus" -Method Get
    Write-Host "✓ Prometheus metrics available" -ForegroundColor Green
} catch {
    Write-Host "✗ Prometheus metrics unavailable" -ForegroundColor Red
}
Write-Host ""

Write-Host "===================================" -ForegroundColor Green
Write-Host "Testing Complete!" -ForegroundColor Green
Write-Host "===================================" -ForegroundColor Green
Write-Host ""
Write-Host "View traces at: http://localhost:9411" -ForegroundColor Cyan
Write-Host "View metrics at: http://localhost:9090" -ForegroundColor Cyan
Write-Host "View dashboards at: http://localhost:3000" -ForegroundColor Cyan
Write-Host ""
