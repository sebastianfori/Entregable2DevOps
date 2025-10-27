# Coffee Shop API

Una API REST para la gestión de productos de una cafetería desarrollada con Spring Boot.

## 🚀 Características

- API REST completa para gestión de productos
- Documentación automática con Swagger UI
- Base de datos H2 en memoria (desarrollo)
- Validación de datos con Bean Validation
- Endpoints para CRUD completo de productos

## 📋 Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- Docker y Docker Compose (para base de datos PostgreSQL)

## 🛠️ Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone <repository-url>
cd coffee_shop
```

### 2. Instalar dependencias
```bash
mvn clean install
```

## 🚀 Ejecución

### Opción 1: Con base de datos H2 (desarrollo)
```bash
mvn spring-boot:run
```

### Opción 2: Con PostgreSQL (producción)
```bash
# Iniciar base de datos
docker compose up -d db

# Ejecutar aplicación con perfil local
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Opción 3: Especificar puerto personalizado
```bash
# Si el puerto 8080 está ocupado
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
```

## 🌐 Acceso a la Aplicación

Una vez ejecutándose, la aplicación estará disponible en:

- **Puerto por defecto**: 8080
- **Puerto alternativo**: 8082 (si especificaste uno diferente)

### URLs importantes:

1. **Swagger UI** (Documentación de la API):
   ```
    http://localhost:8082/swagger-ui/index.html
   ```

2. **Health Check**:
   ```
      http://localhost:8082/api/products/health
      http://localhost:8082/api/clients/health
   ```

3. **API Base**:
   ```
      http://localhost:8082/api/products
      http://localhost:8082/api/clients
   ```

## 📚 Endpoints Disponibles

### Productos
- `GET /api/products` - Obtener todos los productos
- `GET /api/products/{id}` - Obtener producto por ID
- `GET /api/products/category/{category}` - Obtener productos por categoría
- `GET /api/products/available` - Obtener productos disponibles
- `GET /api/products/search?name={name}` - Buscar productos por nombre
- `GET /api/products/price-range?minPrice={min}&maxPrice={max}` - Buscar por rango de precio
- `POST /api/products` - Crear nuevo producto
- `PUT /api/products/{id}` - Actualizar producto
- `DELETE /api/products/{id}` - Eliminar producto
- `PATCH /api/products/{id}/toggle-availability` - Cambiar disponibilidad

### Clientes
- `GET /api/clients` - Obtener todos los clientes
- `GET /api/clients/{id}` - Obtener cliente por ID
- `GET /api/clients/active` - Obtener clientes activos
- `GET /api/clients/search?firstName={firstName}` - Buscar clientes por nombre
- `GET /api/clients/search?lastName={lastName}` - Buscar clientes por apellido
- `POST /api/clients` - Crear nuevo cliente
- `PUT /api/clients/{id}` - Actualizar cliente
- `DELETE /api/clients/{id}` - Eliminar cliente
- `PATCH /api/clients/{id}/toggle-availability` - Cambiar disponibilidad

### Health Check
- `GET /api/products/health` - Verificar estado del controlador

## 🗄️ Base de Datos

### H2 (Desarrollo)
- Base de datos en memoria
- Se reinicia cada vez que ejecutas la aplicación
- Consola web disponible en: `http://localhost:8082/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Usuario: `sa`
  - Contraseña: (vacío)

### PostgreSQL (Producción)
- Configurado en Docker Compose
- Puerto: 5432
- Base de datos: `coffee_shop`
- Usuario: `coffee`
- Contraseña: `coffee123`

## 🔧 Configuración

### Perfiles de Spring Boot
- `default`: Usa H2 en memoria
- `local`: Usa PostgreSQL local
- `docker`: Usa PostgreSQL en Docker

### Archivos de configuración
- `application.properties`: Configuración base
- `application-local.properties`: Configuración para desarrollo local
- `application-docker.properties`: Configuración para Docker

## 🐳 Docker

### Ejecutar solo la base de datos
```bash
docker compose up -d postgres
```

### Ejecutar toda la aplicación con monitoreo
```bash
# Opción 1: Script automatizado
./start-local.sh

# Opción 2: Manual
docker compose up
```

### Servicios incluidos
- **Coffee Shop API**: Puerto 8080
- **PostgreSQL**: Puerto 5432
- **Prometheus**: Puerto 9090 (métricas cada 5 segundos)
- **Grafana**: Puerto 3000 (admin/admin123)

## ☸️ Kubernetes Deployment

### Prerrequisitos
- Kubernetes cluster (minikube, kind, o cluster de producción)
- kubectl configurado
- Docker image construida: `coffee-shop:latest`

### Desplegar en Kubernetes
```bash
# Opción 1: Script automatizado
./deploy-k8s.sh

# Opción 2: Manual
kubectl apply -f k8s/namespace.yml
kubectl apply -f k8s/postgres-deployment.yml
kubectl apply -f k8s/coffee-shop-deployment.yml
kubectl apply -f k8s/prometheus-deployment.yml
kubectl apply -f k8s/grafana-deployment.yml
```

### Acceder a los servicios
```bash
# Coffee Shop API
kubectl port-forward -n coffee-shop svc/coffee-shop-service 8080:8080

# Prometheus
kubectl port-forward -n coffee-shop svc/prometheus-service 9090:9090

# Grafana
kubectl port-forward -n coffee-shop svc/grafana-service 3000:3000
```

### Verificar el despliegue
```bash
kubectl get pods -n coffee-shop
kubectl get services -n coffee-shop
```

## 📊 Monitoreo y Métricas

### Prometheus
- **URL**: http://localhost:9090
- **Scraping**: Cada 5 segundos desde `/actuator/prometheus`
- **Métricas personalizadas**:
  - `coffee_orders_created_total`: Total de pedidos creados
  - `coffee_orders_delivered_total`: Total de pedidos entregados

### Grafana
- **URL**: http://localhost:3000
- **Usuario**: admin
- **Contraseña**: admin123
- **Fuente de datos**: Prometheus (configurada automáticamente)
- **Dashboard**: Coffee Shop Monitoring Dashboard (configurado automáticamente)

### Dashboards Incluidos
El dashboard "Coffee Shop Monitoring Dashboard" incluye:

1. **Requests Per Second (RPS)**: Tasa de requests por segundo
2. **Average Response Time**: Latencia promedio de endpoints
3. **Coffee Orders Created**: Cantidad total de pedidos creados (`coffee_orders_created_total`)
4. **Coffee Orders Delivered**: Cantidad total de pedidos entregados (`coffee_orders_delivered_total`)
5. **JVM Memory Used**: Métricas de memoria JVM (`jvm_memory_used_bytes`)
6. **RPS Over Time**: Gráfico temporal de requests por segundo
7. **Response Time Percentiles**: Percentiles de tiempo de respuesta (50th, 95th, 99th)
8. **Orders Created Over Time**: Gráfico temporal de pedidos creados
9. **Orders Delivered Over Time**: Gráfico temporal de pedidos entregados
10. **JVM Memory Usage Over Time**: Gráfico temporal de uso de memoria JVM

### Generar Datos de Prueba
Para poblar los dashboards con datos de prueba:

```bash
./generate-test-metrics.sh
```

Este script:
- Crea 10 pedidos de prueba
- Actualiza estados de pedidos
- Genera carga en la API
- Permite visualizar las métricas en tiempo real

## 📖 Uso de Swagger UI

1. Ve a `http://localhost:8082/swagger-ui/index.html`
2. Explora los endpoints disponibles
3. Haz clic en "Try it out" para probar cualquier endpoint
4. Completa los parámetros requeridos
5. Ejecuta la petición y ve la respuesta

## 🛠️ Desarrollo

### Estructura del proyecto
```
src/main/java/com/devops/coffee_shop/
├── coffee/
│   ├── controller/     # Controladores REST
│   ├── domain/         # Entidades JPA
│   ├── dto/           # Objetos de transferencia de datos
│   ├── repository/    # Repositorios de datos
│   └── service/       # Lógica de negocio
├── config/            # Configuraciones
└── CoffeeShopApplication.java
```

### Agregar nuevos endpoints
1. Crea el método en `ProductController.java`
2. Agrega anotaciones de Swagger (`@Operation`, `@ApiResponse`)
3. Implementa la lógica en `ProductService.java`
4. La documentación se actualizará automáticamente

## 🚨 Solución de Problemas

### Puerto ocupado
```bash
# Verificar qué proceso usa el puerto
lsof -ti:8080

# Matar el proceso
kill -9 <PID>

# O usar un puerto diferente
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
```

### Error de validación
Si ves errores de Bean Validation, asegúrate de que la dependencia esté en el `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

## 📝 Ejemplo de uso con curl

```bash
# Obtener todos los productos
curl http://localhost:8082/api/products

# Crear un nuevo producto
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Café Americano",
    "description": "Café negro americano",
    "price": 2.50,
    "category": "BEVERAGE",
    "available": true
  }'

# Buscar productos
curl "http://localhost:8082/api/products/search?name=café"

# -------- Clientes --------

# Obtener todos los clientes
curl http://localhost:8082/api/clients

# Crear un nuevo cliente
curl -X POST http://localhost:8082/api/clients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "Pérez",
    "documentNumber": "4.123.456-7",
    "birthDate": "1990-05-12",
    "active": true
  }'

# Buscar clientes por nombre
curl "http://localhost:8082/api/clients/search?firstName=juan"

# Buscar clientes por apellido
curl "http://localhost:8082/api/clients/search?lastName=perez"

# Activar/desactivar un cliente
curl -X PATCH http://localhost:8082/api/clients/1/toggle-availability
```

## 📄 Créditos

Este proyecto fue realizado por Matías Ferreira, Sebastián Forische y Lucas Martino para la asignatura DevOps de la Universidad Católica del Uruguay
