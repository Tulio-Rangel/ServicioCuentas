# Servicio de Gestión de Cuentas Bancarias

## Descripción
Este microservicio proporciona una API REST para la gestión completa de cuentas bancarias y sus movimientos financieros. Permite crear y gestionar cuentas de ahorro y corrientes, realizar depósitos y retiros, y generar reportes de estado de cuenta.

## Funcionalidades principales

### Gestión de Cuentas
- Crear cuentas de ahorro o corrientes
- Consultar información de cuentas
- Eliminar cuentas
- Validación de clientes mediante comunicación asíncrona con microservicio de clientes

### Gestión de Movimientos
- Realizar depósitos en cuentas
- Realizar retiros con validación de saldo disponible
- Eliminación de movimientos
- Actualización automática de saldos

### Reportes
- Generación de reportes de estado de cuenta
- Filtrado por rango de fechas
- Incluye información de cliente, cuentas y movimientos

## Arquitectura

El proyecto sigue una arquitectura en capas:

### Controladores
- `CuentaController`: Gestiona los endpoints para operaciones CRUD de cuentas y reportes
- `MovimientoController`: Gestiona los endpoints para crear y eliminar movimientos

### Servicios
- `CuentaService`: Implementa la lógica de negocio para cuentas y generación de reportes
- `MovimientoService`: Implementa la lógica para realizar movimientos y actualizar saldos

### Repositorios
- `CuentaRepository`: Interfaz para persistencia de cuentas
- `MovimientoRepository`: Interfaz para persistencia de movimientos

### Modelos
- `Cuenta`: Entidad que representa una cuenta bancaria
- `Movimiento`: Entidad que representa un movimiento financiero
- `ReporteEstadoCuenta`: DTO para generación de reportes

### Mensajería
- `CuentaProducer`: Envía mensajes para validar clientes
- `CuentaConsumer`: Recibe respuestas de validación

### Manejo de Excepciones
- `ResourceNotFoundException`: Para recursos no encontrados
- `SaldoNoDisponibleException`: Para rechazar retiros con saldo insuficiente
- `MovimientoIvalidoException`: Para validaciones de movimientos
- `GlobalExceptionHandler`: Manejo centralizado de excepciones

## Endpoints REST

### Cuentas
```
POST /cuentas - Crear una nueva cuenta
GET /cuentas/{id} - Obtener cuenta por ID
DELETE /cuentas/{id} - Eliminar una cuenta
GET /cuentas/reporte - Generar reporte de estado de cuenta (parámetros: clienteId, fechaInicio, fechaFin)
```

### Movimientos
```
POST /movimientos/cuenta/{cuentaId} - Crear un movimiento (depósito o retiro)
DELETE /movimientos/{id} - Eliminar un movimiento
```

## Tecnologías utilizadas

- **Java 17**: Lenguaje de programación base
- **Spring Boot 3.4.3**: Framework para desarrollo de aplicaciones
- **Spring Data JPA**: Para persistencia de datos
- **Spring Web**: Para creación de APIs RESTful
- **H2 Database**: Base de datos en memoria para pruebas y desarrollo
- **ActiveMQ**: Broker de mensajería para comunicación asíncrona entre servicios
- **Lombok**: Reducción de código boilerplate
- **Validation**: Validación de datos de entrada
- **Spring Boot DevTools**: Herramientas para desarrollo

## Configuración

El servicio se ejecuta en el puerto 8081 y utiliza una base de datos H2 en memoria. La comunicación con otros microservicios se realiza mediante ActiveMQ.

Configuración principal (application.properties):
```
server.port=8081
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.activemq.broker-url=tcp://localhost:61616
spring.activemq.user=admin
spring.activemq.password=admin
```

## Ejecución del proyecto

### Requisitos previos
- Java 17 o superior
- Gradle
- ActiveMQ (para comunicación entre servicios)

### Pasos para ejecutar
1. Clonar el repositorio:
```
git clone <URL-del-repositorio>
cd servicio-cuentas
```

2. Compilar el proyecto:
```
./gradlew build
```

3. Ejecutar la aplicación:
```
./gradlew bootRun
```
o
```
java -jar build/libs/servicio-cuentas-0.0.1-SNAPSHOT.jar
```

4. La API estará disponible en:
```
http://localhost:8081
```

## Ejemplos de uso

### Crear una cuenta
```
POST /cuentas
Content-Type: application/json

{
"numeroCuenta": "12345",
"tipoCuenta": "Ahorro",
"saldoInicial": 1000.0,
"estado": true,
"clienteId": 1
}
```

### Realizar un depósito
```
POST /movimientos/cuenta/1
Content-Type: application/json

{
"fecha": "2023-05-15",
"tipoMovimiento": "Depósito",
"valor": 500.0
}
```

### Realizar un retiro
```
POST /movimientos/cuenta/1
Content-Type: application/json

{
"fecha": "2023-05-16",
"tipoMovimiento": "Retiro",
"valor": 200.0
}
```

### Generar reporte
```
GET /cuentas/reporte?clienteId=1&fechaInicio=2023-05-01&fechaFin=2023-05-30
```

