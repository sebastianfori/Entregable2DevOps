#!/bin/bash

# Coffee Shop Metrics Test Script
# This script generates test data to populate the Grafana dashboards

echo "🧪 Generating test metrics for Coffee Shop..."

# Base URL
BASE_URL="http://localhost:8080"

# Function to make API calls
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    
    if [ "$method" = "POST" ]; then
        curl -s -X POST "$BASE_URL$endpoint" \
             -H "Content-Type: application/json" \
             -d "$data" > /dev/null
    else
        curl -s -X "$method" "$BASE_URL$endpoint" > /dev/null
    fi
}

# Function to create test orders
create_test_orders() {
    echo "📝 Creating test orders..."
    
    # Create multiple orders
    for i in {1..10}; do
        make_request "POST" "/api/orders" "{
            \"customerName\": \"Customer $i\",
            \"drink\": \"latte\",
            \"quantity\": $((RANDOM % 3 + 1))
        }"
        sleep 0.5
    done
    
    echo "✅ Created 10 test orders"
}

# Function to update order statuses
update_order_statuses() {
    echo "🔄 Updating order statuses..."
    
    # Get all orders and update their statuses
    for i in {1..5}; do
        # Update to IN_PROGRESS
        make_request "PATCH" "/api/orders/$i/status/IN_PROGRESS" ""
        sleep 0.2
        
        # Update to READY
        make_request "PATCH" "/api/orders/$i/status/READY" ""
        sleep 0.2
        
        # Update to DELIVERED
        make_request "PATCH" "/api/orders/$i/status/DELIVERED" ""
        sleep 0.2
    done
    
    echo "✅ Updated order statuses"
}

# Function to generate API load
generate_api_load() {
    echo "🚀 Generating API load..."
    
    for i in {1..20}; do
        # Get all orders
        make_request "GET" "/api/orders" ""
        
        # Get individual orders
        make_request "GET" "/api/orders/1" ""
        make_request "GET" "/api/orders/2" ""
        
        # Health check
        make_request "GET" "/actuator/health" ""
        
        sleep 0.1
    done
    
    echo "✅ Generated API load"
}

# Main execution
echo "🎯 Starting metrics generation..."

# Check if the API is running
if ! curl -s "$BASE_URL/actuator/health" > /dev/null; then
    echo "❌ Coffee Shop API is not running. Please start it first with:"
    echo "   ./start-local.sh"
    exit 1
fi

# Generate test data
create_test_orders
update_order_statuses
generate_api_load

echo ""
echo "🎉 Test data generation completed!"
echo ""
echo "📊 You can now view the metrics in Grafana:"
echo "   http://localhost:3000 (admin/admin123)"
echo ""
echo "🔍 Check Prometheus for raw metrics:"
echo "   http://localhost:9090"
echo ""
echo "📈 The dashboard should now show:"
echo "   - Requests Per Second (RPS)"
echo "   - Average Response Time"
echo "   - Coffee Orders Created/Delivered"
echo "   - JVM Memory Usage"
