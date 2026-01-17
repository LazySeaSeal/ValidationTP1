# 🚀 Quick Setup Script

Write-Host "===================================" -ForegroundColor Green
Write-Host "Microservices Architecture Setup" -ForegroundColor Green
Write-Host "===================================" -ForegroundColor Green
Write-Host ""

# Check prerequisites
Write-Host "Checking prerequisites..." -ForegroundColor Yellow
Write-Host ""

# Check Java
$javaVersion = java -version 2>&1 | Select-String "version"
if ($javaVersion) {
    Write-Host "✓ Java found: $javaVersion" -ForegroundColor Green
} else {
    Write-Host "✗ Java 17 required!" -ForegroundColor Red
    exit 1
}

# Check Maven
$mvnVersion = mvn -version 2>&1 | Select-String "Apache Maven"
if ($mvnVersion) {
    Write-Host "✓ Maven found: $mvnVersion" -ForegroundColor Green
} else {
    Write-Host "✗ Maven required!" -ForegroundColor Red
    exit 1
}

# Check Docker
$dockerVersion = docker --version 2>&1
if ($dockerVersion) {
    Write-Host "✓ Docker found: $dockerVersion" -ForegroundColor Green
} else {
    Write-Host "⚠ Docker not found - you'll need to run services manually" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "===================================" -ForegroundColor Cyan
Write-Host "Building All Services..." -ForegroundColor Cyan
Write-Host "===================================" -ForegroundColor Cyan
Write-Host ""

$services = @(
    "discovery-service",
    "config-server",
    "api-gateway",
    "student-service",
    "department-service",
    "enrollment-service"
)

foreach ($service in $services) {
    Write-Host "Building $service..." -ForegroundColor Yellow
    Set-Location $service
    mvn clean package -DskipTests
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ $service built successfully" -ForegroundColor Green
    } else {
        Write-Host "✗ $service build failed" -ForegroundColor Red
        Set-Location ..
        exit 1
    }
    Set-Location ..
    Write-Host ""
}

Write-Host "===================================" -ForegroundColor Green
Write-Host "All services built successfully!" -ForegroundColor Green
Write-Host "===================================" -ForegroundColor Green
Write-Host ""

Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. With Docker: Run 'docker-compose up -d'" -ForegroundColor White
Write-Host "2. Without Docker: Follow the manual startup guide in MICROSERVICES-README.md" -ForegroundColor White
Write-Host ""
Write-Host "Access URLs:" -ForegroundColor Cyan
Write-Host "  - Eureka Dashboard: http://localhost:8761" -ForegroundColor White
Write-Host "  - API Gateway: http://localhost:8080" -ForegroundColor White
Write-Host "  - Zipkin Tracing: http://localhost:9411" -ForegroundColor White
Write-Host "  - Prometheus: http://localhost:9090" -ForegroundColor White
Write-Host "  - Grafana: http://localhost:3000 (admin/admin)" -ForegroundColor White
Write-Host "  - Axon Server: http://localhost:8024" -ForegroundColor White
Write-Host ""
