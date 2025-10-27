# Laboratorio 2: API de pedidos de Cafetería

## 📋 Resumen Ejecutivo

Este laboratorio implementa una **API REST completo para gestión de pedidos de cafetería** con **monitoreo DevOps** usando Spring Boot, Prometheus y Grafana.

### ✅ Requisitos Cumplidos

**Funcionales:**
- ✅ API REST completa para gestión de pedidos (CRUD)
- ✅ Endpoints: POST, GET, PATCH, DELETE para pedidos
- ✅ Estados de pedidos: NEW, IN_PROGRESS, READY, DELIVERED, CANCELED

**No Funcionales:**
- ✅ Java 17 + Spring Boot 3.0+
- ✅ Métricas Prometheus en `/actuator/prometheus`
- ✅ Contadores personalizados: `coffee_orders_created_total`, `coffee_orders_delivered_total`
- ✅ Scraping cada 5 segundos
- ✅ Dashboards Grafana funcionales
- ✅ Containerización con Docker
- ✅ Despliegue en Kubernetes

### 🎯 Métricas Implementadas

- **RPS (Requests Per Second)**: `sum(rate(http_server_requests_seconds_count[5m]))`
- **Latencia promedio**: `sum(rate(http_server_requests_seconds_sum[5m])) / sum(rate(http_server_requests_seconds_count[5m]))`
- **Pedidos creados**: `coffee_orders_total`
- **Pedidos entregados**: `coffee_orders_delivered_total`
- **Memoria JVM**: `jvm_memory_used_bytes{area="heap"}` y `jvm_memory_used_bytes{area="nonheap"}`

---

## 🚀 Guía Rápida de inicio

### 1. Iniciar el Laboratorio
```bash
# Clonar y ejecutar
git clone <url-repo>
cd entregable2devops
./start-local.sh
```

### 2. Acceder a los Recursos
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Métricas**: http://localhost:8080/actuator/prometheus
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (Credenciales: admin/admin123)

### 3. Ver Dashboards
1. Ir a http://localhost:3000
2. Login: admin/admin123
3. Buscar "Coffee Shop Monitoring Dashboard"
4. Ver métricas en tiempo real

### 4. Ejecutar Validación
```bash
./validate-lab.sh
```

### 5. Generar Datos de Prueba
```bash
./generate-test-metrics.sh
```

---

## 📚 Estructura del Laboratorio

### Parte 1: API REST
**Objetivo**: Desarrollar API REST para gestión de pedidos de cafetería

**Implementación**:
- Spring Boot 3.0+ con Java 17
- Endpoints CRUD completos para pedidos
- Validación de datos con Bean Validation
- Documentación automática con Swagger UI

**Endpoints Implementados**:
```
POST   /api/orders                    # Crear pedido
GET    /api/orders                    # Listar todos los pedidos
GET    /api/orders/{id}               # Obtener pedido por ID
PATCH  /api/orders/{id}/status/{status} # Actualizar estado
DELETE /api/orders/{id}               # Eliminar pedido
```

**Estructura de Pedido**:
```json
{
  "customerName": "Ana",
  "drink": "latte",
  "quantity": 2,
  "status": "NEW",
  "createdAt": "2025-10-27T00:23:02.161309"
}
```

**Estados de Pedido**:
- `NEW`: Pedido creado
- `IN_PROGRESS`: En preparación
- `READY`: Listo para entrega
- `DELIVERED`: Entregado
- `CANCELED`: Cancelado

### Parte 2: Métricas y Observabilidad
**Objetivo**: Exponer métricas de aplicación e infraestructura mediante Micrometer + Prometheus

**Implementación**:
- Spring Boot Actuator habilitado
- Endpoint `/actuator/prometheus` expuesto
- Métricas personalizadas con Micrometer
- Contadores de negocio implementados

**Métricas Personalizadas**:
```java
@Component
public class CoffeeMetrics {
    private final Counter ordersCreatedCounter;
    private final Counter ordersDeliveredCounter;
    
    public void incrementOrdersCreated() {
        ordersCreatedCounter.increment();
    }
    
    public void incrementOrdersDelivered() {
        ordersDeliveredCounter.increment();
    }
}
```

**Métricas Expuestas**:
- `coffee_orders_total`: Total de pedidos creados
- `coffee_orders_delivered_total`: Total de pedidos entregados
- `http_server_requests_seconds_count`: Conteo de requests HTTP
- `http_server_requests_seconds_sum`: Suma de tiempo de respuesta
- `jvm_memory_used_bytes`: Uso de memoria JVM

### Parte 3: Integración Prometheus y Grafana
**Objetivo**: Integrar Prometheus y Grafana para scraping y visualización de métricas

**Configuración Prometheus** (`prometheus.yml`):
```yaml
scrape_configs:
  - job_name: 'coffee-shop'
    scrape_interval: 5s
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['coffee-shop:8080']
```

**Configuración Grafana**:
- Datasource Prometheus configurado automáticamente
- Dashboard provisioning habilitado
- Configuración persistente con volúmenes Docker

**Docker Compose**:
```yaml
services:
  coffee-shop-app:
    build: .
    ports:
      - "8080:8080"
  
  prometheus:
    image: prom/prometheus:latest
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
  
  grafana:
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin123
```

### Parte 4: Dashboards Grafana
**Objetivo**: Visualizar métricas requeridas en dashboards funcionales

**Dashboard Implementado**: "Coffee Shop Monitoring Dashboard"

**Paneles Incluidos**:

1. **Requests Per Second (RPS)**
   - Query: `sum(rate(http_server_requests_seconds_count[5m]))`
   - Tipo: Stat panel
   - Unidad: reqps

2. **Average Response Time**
   - Query: `sum(rate(http_server_requests_seconds_sum[5m])) / sum(rate(http_server_requests_seconds_count[5m]))`
   - Tipo: Stat panel
   - Unidad: segundos

3. **Coffee Orders Created**
   - Query: `coffee_orders_total`
   - Tipo: Stat panel
   - Unidad: número

4. **Coffee Orders Delivered**
   - Query: `coffee_orders_delivered_total`
   - Tipo: Stat panel
   - Unidad: número

5. **JVM Memory Used**
   - Query: `jvm_memory_used_bytes{area="heap"}`
   - Tipo: Stat panel
   - Unidad: bytes

6. **JVM Memory Non-Heap Used**
   - Query: `jvm_memory_used_bytes{area="nonheap"}`
   - Tipo: Stat panel
   - Unidad: bytes

7. **RPS Over Time**
   - Query: `sum(rate(http_server_requests_seconds_count[1m]))`
   - Tipo: Time series
   - Unidad: reqps

8. **Response Time Over Time**
   - Query: `sum(rate(http_server_requests_seconds_sum[5m])) / sum(rate(http_server_requests_seconds_count[5m]))`
   - Tipo: Time series
   - Unidad: segundos

9. **Orders Created Over Time**
   - Query: `coffee_orders_total`
   - Tipo: Time series
   - Unidad: número

10. **Orders Delivered Over Time**
    - Query: `coffee_orders_delivered_total`
    - Tipo: Time series
    - Unidad: número

11. **JVM Memory Usage Over Time**
    - Query: `jvm_memory_used_bytes{area="heap"}` y `jvm_memory_used_bytes{area="nonheap"}`
    - Tipo: Time series
    - Unidad: bytes

**Configuración del Dashboard**:
- Refresh automático cada 5 segundos
- Rango de tiempo: Última hora
- Estilo: Dark theme
- Tags: coffee-shop, monitoring

---

## 🐳 Containerización y Despliegue

### Docker Compose (Desarrollo Local)
```bash
# Iniciar todos los servicios
./start-local.sh

# O manualmente
docker compose up -d
```

**Servicios**:
- `coffee-shop-app`: API Spring Boot (puerto 8080)
- `postgres`: Base de datos PostgreSQL (puerto 5432)
- `prometheus`: Servidor de métricas (puerto 9090)
- `grafana`: Servidor de dashboards (puerto 3000)

### Kubernetes (Producción)
```bash
# Desplegar en Kubernetes
./deploy-k8s.sh

# O manualmente
kubectl apply -f k8s/namespace.yml
kubectl apply -f k8s/postgres-deployment.yml
kubectl apply -f k8s/coffee-shop-deployment.yml
kubectl apply -f k8s/prometheus-deployment.yml
kubectl apply -f k8s/grafana-deployment.yml
```

**Recursos Kubernetes**:
- Namespace: `coffee-shop`
- Deployments: coffee-shop, postgres, prometheus, grafana
- Services: Exposición de puertos
- ConfigMaps: Configuraciones de Prometheus y Grafana
- PersistentVolumeClaims: Almacenamiento persistente

---

## 🧪 Testing y Validación

### Script de Validación Automática
```bash
./validate-lab.sh
```

**Tests Ejecutados** (21 tests total):

**1. API Endpoints (6 tests)**:
- ✅ Coffee Shop API is running
- ✅ GET /api/orders - List all orders
- ✅ GET /api/orders/{id} - Get order by ID
- ✅ POST /api/orders - Create order
- ✅ PATCH /api/orders/{id}/status/{status} - Update order status
- ✅ DELETE /api/orders/{id} - Delete order

**2. Métricas (6 tests)**:
- ✅ Metrics endpoint accessible
- ✅ Coffee orders created metric exists
- ✅ Coffee orders delivered metric exists
- ✅ HTTP requests count metric exists
- ✅ HTTP requests sum metric exists
- ✅ JVM memory metric exists

**3. Prometheus (5 tests)**:
- ✅ Prometheus is running
- ✅ Prometheus can query coffee orders metric
- ✅ Prometheus can query coffee orders delivered metric
- ✅ Prometheus can query HTTP requests count
- ✅ Prometheus can query JVM memory metric
- ✅ Prometheus targets are healthy

**4. Grafana (3 tests)**:
- ✅ Grafana is running
- ✅ Grafana can access Prometheus
- ✅ Coffee Shop dashboard exists

### Generación de Datos de Prueba
```bash
./generate-test-metrics.sh
```

**Funcionalidades**:
- Crea 10 pedidos de prueba
- Actualiza estados de pedidos
- Genera carga en la API
- Permite visualizar métricas en tiempo real

---

## 📊 Monitoreo en Tiempo Real

### Acceso a Dashboards
1. **Grafana**: http://localhost:3000
   - Usuario: `admin`
   - Contraseña: `admin123`
   - Dashboard: "Coffee Shop Monitoring Dashboard"

2. **Prometheus**: http://localhost:9090
   - Queries directas
   - Targets status
   - Métricas raw

3. **API Metrics**: http://localhost:8080/actuator/prometheus
   - Métricas en formato Prometheus
   - Endpoint de scraping

### Métricas en Vivo
- **Scraping**: Cada 5 segundos
- **Refresh**: Dashboard se actualiza automáticamente
- **Persistencia**: Datos almacenados en Prometheus
- **Visualización**: Gráficos en tiempo real en Grafana

---

## 🛠️ Desarrollo y Estructura

### Estructura del Proyecto
```
src/main/java/com/devops/coffee_shop/
├── coffee/
│   ├── controller/     # OrderController.java
│   ├── domain/         # Order.java, OrderStatus.java
│   ├── dto/           # OrderDto.java
│   ├── repository/    # OrderRepository.java
│   ├── service/       # OrderService.java
│   └── metrics/       # CoffeeMetrics.java
├── config/            # Configuraciones
└── CoffeeShopApplication.java
```

### Archivos de Configuración
- `application.properties`: Configuración base Spring Boot
- `prometheus.yml`: Configuración de scraping
- `grafana-datasources.yml`: Datasource provisioning
- `grafana-dashboard-provisioning.yml`: Dashboard provisioning
- `grafana-dashboard.json`: Definición del dashboard
- `docker-compose.yml`: Orquestación de servicios
- `Dockerfile`: Imagen de la aplicación

### Scripts de Automatización
- `start-local.sh`: Iniciar desarrollo local
- `deploy-k8s.sh`: Desplegar en Kubernetes
- `validate-lab.sh`: Validación completa
- `generate-test-metrics.sh`: Generar datos de prueba

---

## 📝 Ejemplos de Uso

### Crear un Pedido
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Ana García",
    "drink": "latte",
    "quantity": 2
  }'
```

### Listar Pedidos
```bash
curl http://localhost:8080/api/orders
```

### Actualizar Estado
```bash
curl -X PATCH http://localhost:8080/api/orders/1/status/DELIVERED
```

### Ver Métricas
```bash
curl http://localhost:8080/actuator/prometheus | grep coffee_orders
```

---

## 🚨 Solución de Problemas

### Puerto Ocupado
```bash
# Verificar proceso
lsof -ti:8080

# Matar proceso
kill -9 <PID>

# Usar puerto diferente
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
```

### Dashboard No Carga
```bash
# Verificar logs de Grafana
docker logs grafana

# Reiniciar Grafana
docker compose restart grafana
```

### Métricas No Aparecen
```bash
# Verificar Prometheus targets
curl http://localhost:9090/api/v1/targets

# Verificar métricas de la aplicación
curl http://localhost:8080/actuator/prometheus
```

---

## 📄 Créditos

Este proyecto fue realizado por **Matías Ferreira**, **Sebastián Forische** y **Lucas Martino** para la asignatura DevOps de la Universidad Católica del Uruguay.

**Laboratorio 2 - DevOps**: API de pedidos de cafetería.