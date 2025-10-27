#!/bin/bash

# Coffee Shop Local Development Script
# This script starts the coffee shop application with Prometheus and Grafana using Docker Compose

echo "🚀 Starting Coffee Shop with Prometheus and Grafana..."

# Build the application
echo "🔨 Building Coffee Shop application..."
docker build -t coffee-shop:latest .

# Start all services
echo "🐳 Starting all services with Docker Compose..."
docker compose up -d

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 10

# Check if services are running
echo "📊 Checking service status..."
docker compose ps

echo "✅ All services started successfully!"
echo ""
echo "🌐 Access URLs:"
echo "  Coffee Shop API: http://localhost:8080"
echo "  Swagger UI: http://localhost:8080/swagger-ui/index.html"
echo "  Prometheus: http://localhost:9090"
echo "  Grafana: http://localhost:3000 (admin/admin123)"
echo ""
echo "📊 Metrics endpoint:"
echo "  http://localhost:8080/actuator/prometheus"
echo ""
echo "🛑 To stop all services:"
echo "  docker compose down"
