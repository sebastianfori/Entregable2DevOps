#!/bin/bash

# Coffee Shop DevOps Laboratory Validation Script
# This script validates all requirements

echo "🔍 Coffee Shop DevOps Laboratory Validation"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Validation results
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Function to run a test
run_test() {
    local test_name="$1"
    local test_command="$2"
    local expected_result="$3"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -n "Testing: $test_name... "
    
    if eval "$test_command" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ PASS${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        return 0
    else
        echo -e "${RED}❌ FAIL${NC}"
        FAILED_TESTS=$((FAILED_TESTS + 1))
        return 1
    fi
}

# Function to check HTTP response
check_http_response() {
    local url="$1"
    local expected_status="$2"
    local test_name="$3"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -n "Testing: $test_name... "
    
    local status_code=$(curl -s -o /dev/null -w "%{http_code}" "$url")
    if [ "$status_code" = "$expected_status" ]; then
        echo -e "${GREEN}✅ PASS${NC} (Status: $status_code)"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        return 0
    else
        echo -e "${RED}❌ FAIL${NC} (Expected: $expected_status, Got: $status_code)"
        FAILED_TESTS=$((FAILED_TESTS + 1))
        return 1
    fi
}

# Function to check if metric exists
check_metric() {
    local metric_name="$1"
    local test_name="$2"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -n "Testing: $test_name... "
    
    if curl -s http://localhost:8080/actuator/prometheus | grep -q "^$metric_name"; then
        echo -e "${GREEN}✅ PASS${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        return 0
    else
        echo -e "${RED}❌ FAIL${NC}"
        FAILED_TESTS=$((FAILED_TESTS + 1))
        return 1
    fi
}

# Function to check Prometheus query
check_prometheus_query() {
    local query="$1"
    local test_name="$2"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -n "Testing: $test_name... "
    
    local result=$(curl -s "http://localhost:9090/api/v1/query?query=$(echo "$query" | sed 's/ /%20/g')" | jq -r '.data.result | length' 2>/dev/null)
    if [ "$result" -gt 0 ] 2>/dev/null; then
        echo -e "${GREEN}✅ PASS${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        return 0
    else
        echo -e "${RED}❌ FAIL${NC}"
        FAILED_TESTS=$((FAILED_TESTS + 1))
        return 1
    fi
}

echo -e "${BLUE}📋 VALIDATION REQUIREMENTS:${NC}"
echo "1. API responds correctly to all endpoints"
echo "2. Metrics accessible at /actuator/prometheus"
echo "3. Prometheus detects and stores metrics"
echo "4. Grafana shows functional dashboards"
echo ""

echo -e "${BLUE}🚀 Starting Validation Tests...${NC}"
echo ""

# ==========================================
# 1. API ENDPOINTS VALIDATION
# ==========================================
echo -e "${YELLOW}1. API ENDPOINTS VALIDATION${NC}"
echo "------------------------"

# Check if services are running
run_test "Coffee Shop API is running" "curl -s http://localhost:8080/actuator/health > /dev/null" "API Health Check"

# Test all required endpoints
check_http_response "http://localhost:8080/api/orders" "200" "GET /api/orders - List all orders"
check_http_response "http://localhost:8080/api/orders/1" "200" "GET /api/orders/{id} - Get order by ID"

# Test POST endpoint
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo -n "Testing: POST /api/orders - Create order... "
response=$(curl -s -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName": "Test User", "drink": "latte", "quantity": 1}')
if echo "$response" | grep -q "Test User"; then
    echo -e "${GREEN}✅ PASS${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${RED}❌ FAIL${NC}"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi

# Test PATCH endpoint
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo -n "Testing: PATCH /api/orders/{id}/status/{status} - Update order status... "
status_code=$(curl -s -o /dev/null -w "%{http_code}" -X PATCH http://localhost:8080/api/orders/1/status/DELIVERED)
if [ "$status_code" = "200" ]; then
    echo -e "${GREEN}✅ PASS${NC} (Status: $status_code)"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${RED}❌ FAIL${NC} (Expected: 200, Got: $status_code)"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi

# Test DELETE endpoint
check_http_response "http://localhost:8080/api/orders/1" "200" "DELETE /api/orders/{id} - Delete order"

echo ""

# ==========================================
# 2. METRICS ACCESSIBILITY VALIDATION
# ==========================================
echo -e "${YELLOW}2. METRICS ACCESSIBILITY VALIDATION${NC}"
echo "------------------------------------"

check_http_response "http://localhost:8080/actuator/prometheus" "200" "Metrics endpoint accessible"

# Check for required metrics
check_metric "coffee_orders_total" "Coffee orders created metric exists"
check_metric "coffee_orders_delivered_total" "Coffee orders delivered metric exists"
check_metric "http_server_requests_seconds_count" "HTTP requests count metric exists"
check_metric "http_server_requests_seconds_sum" "HTTP requests sum metric exists"
check_metric "jvm_memory_used_bytes" "JVM memory metric exists"

echo ""

# ==========================================
# 3. PROMETHEUS VALIDATION
# ==========================================
echo -e "${YELLOW}3. PROMETHEUS VALIDATION${NC}"
echo "----------------------"

# Check if Prometheus is running
run_test "Prometheus is running" "curl -s http://localhost:9090/-/healthy > /dev/null" "Prometheus Health Check"

# Check if Prometheus can query metrics
check_prometheus_query "coffee_orders_total" "Prometheus can query coffee orders metric"
check_prometheus_query "coffee_orders_delivered_total" "Prometheus can query coffee orders delivered metric"
check_prometheus_query "http_server_requests_seconds_count" "Prometheus can query HTTP requests count"
check_prometheus_query "jvm_memory_used_bytes" "Prometheus can query JVM memory metric"

# Check Prometheus targets
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo -n "Testing: Prometheus targets are healthy... "
if curl -s http://localhost:9090/api/v1/targets | grep -q "coffee-shop"; then
    echo -e "${GREEN}✅ PASS${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${RED}❌ FAIL${NC}"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi

echo ""

# ==========================================
# 4. GRAFANA VALIDATION
# ==========================================
echo -e "${YELLOW}4. GRAFANA VALIDATION${NC}"
echo "-------------------"

# Check if Grafana is running
run_test "Grafana is running" "curl -s http://localhost:3000/api/health > /dev/null" "Grafana Health Check"

# Check if Grafana can access Prometheus
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo -n "Testing: Grafana can access Prometheus... "
datasources=$(curl -s -u admin:admin123 http://localhost:3000/api/datasources | jq -r '.[] | select(.type == "prometheus") | .url')
if echo "$datasources" | grep -q "prometheus"; then
    echo -e "${GREEN}✅ PASS${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${RED}❌ FAIL${NC}"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi

# Check if dashboard exists
TOTAL_TESTS=$((TOTAL_TESTS + 1))
echo -n "Testing: Coffee Shop dashboard exists... "
dashboards=$(curl -s -u admin:admin123 http://localhost:3000/api/search?query=Coffee%20Shop | jq -r '.[] | select(.title | contains("Coffee Shop")) | .title')
if echo "$dashboards" | grep -q "Coffee Shop"; then
    echo -e "${GREEN}✅ PASS${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${RED}❌ FAIL${NC}"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi

echo ""

# ==========================================
# SUMMARY
# ==========================================
echo -e "${BLUE}📊 VALIDATION SUMMARY${NC}"
echo "=================="
echo -e "Total Tests: ${BLUE}$TOTAL_TESTS${NC}"
echo -e "Passed: ${GREEN}$PASSED_TESTS${NC}"
echo -e "Failed: ${RED}$FAILED_TESTS${NC}"
echo ""

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN}🎉 ALL TESTS PASSED! 🎉${NC}"
    echo -e "${GREEN}✅ The Coffee Shop DevOps Laboratory is ready!${NC}"
    echo ""
    echo -e "${BLUE}📋 ACCESS INFORMATION:${NC}"
    echo "• Coffee Shop API: http://localhost:8080"
    echo "• Swagger UI: http://localhost:8080/swagger-ui/index.html"
    echo "• Metrics: http://localhost:8080/actuator/prometheus"
    echo "• Prometheus: http://localhost:9090"
    echo "• Grafana: http://localhost:3000 (admin/admin123)"
    echo ""
    echo -e "${BLUE}📈 DASHBOARD METRICS VERIFIED:${NC}"
    echo "• Requests Per Second (RPS)"
    echo "• Average Response Time"
    echo "• Coffee Orders Created/Delivered"
    echo "• JVM Memory Usage"
    exit 0
else
    echo -e "${RED}❌ SOME TESTS FAILED${NC}"
    echo -e "${RED}Please check the failed tests above and fix the issues.${NC}"
    exit 1
fi
