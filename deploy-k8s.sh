echo "🚀 Deploying Coffee Shop to Kubernetes..."

# Create namespace
echo "📦 Creating namespace..."
kubectl apply -f k8s/namespace.yml

# Deploy PostgreSQL
echo "🐘 Deploying PostgreSQL..."
kubectl apply -f k8s/postgres-deployment.yml

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
kubectl wait --for=condition=ready pod -l app=postgres -n coffee-shop --timeout=60s

# Deploy Coffee Shop Application
echo "☕ Deploying Coffee Shop Application..."
kubectl apply -f k8s/coffee-shop-deployment.yml

# Deploy Prometheus
echo "📊 Deploying Prometheus..."
kubectl apply -f k8s/prometheus-deployment.yml

# Deploy Grafana
echo "📈 Deploying Grafana..."
kubectl apply -f k8s/grafana-deployment.yml

# Wait for all deployments to be ready
echo "⏳ Waiting for all deployments to be ready..."
kubectl wait --for=condition=ready pod -l app=coffee-shop-app -n coffee-shop --timeout=60s
kubectl wait --for=condition=ready pod -l app=prometheus -n coffee-shop --timeout=60s
kubectl wait --for=condition=ready pod -l app=grafana -n coffee-shop --timeout=60s

echo "✅ Deployment completed successfully!"
echo ""
echo "🌐 Access URLs:"
echo "  Coffee Shop API: http://localhost:8080"
echo "  Prometheus: http://localhost:9090"
echo "  Grafana: http://localhost:3000 (admin/admin123)"
echo ""
echo "📊 To access services from outside the cluster:"
echo "  kubectl port-forward -n coffee-shop svc/coffee-shop-service 8080:8080"
echo "  kubectl port-forward -n coffee-shop svc/prometheus-service 9090:9090"
echo "  kubectl port-forward -n coffee-shop svc/grafana-service 3000:3000"
