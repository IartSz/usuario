# BookPoint · Microservicio de Usuario (`ms-usuario`)

Microservicio encargado de la gestión de usuarios dentro del sistema **BookPoint**. Expone una API REST con soporte **HATEOAS** y actúa como servicio **proveedor**: mantiene los datos de los usuarios y es consultado por otros microservicios que necesitan validar su existencia o verificar sus credenciales.

---

## 🛠️ Tecnologías

- **Java 17+**
- **Spring Boot 4**
- **Spring Web (MVC)**
- **Spring HATEOAS** — respuestas con `EntityModel` / `CollectionModel`
- **Spring Data JPA**
- **MySQL** (entorno de producción/desarrollo)
- **H2** (base de datos en memoria para tests)
- **SpringDoc OpenAPI / Swagger** — documentación de la API
- **JaCoCo** — reportes de cobertura de tests
- **JUnit 5 + Mockito** — pruebas unitarias e integración

---

## 🏗️ Rol en la arquitectura

`ms-usuario` es un servicio **proveedor**: no consume a otros microservicios. Es consultado por los servicios que necesitan información del usuario.

| Microservicio        | Para qué consulta a `ms-usuario`           |
| -------------------- | ------------------------------------------ |
| `ms-autentificacion` | Verificar credenciales (correo y password) |
| `ms-carro`           | Validar que el usuario exista              |
| `ms-pedidos`         | Validar que el usuario exista              |

```
                         ms-autentificacion ─┐
                         ms-carro ───────────┼──► ms-usuario (8083)
                         ms-pedidos ─────────┘   (consultan vía RestTemplate)

Cliente → Gateway (8080) → ms-usuario (8083)
```

---

## ✅ Requisitos previos

- JDK 17 o superior
- Maven 3.8+
- MySQL en ejecución (para el perfil por defecto)

---

## ⚙️ Configuración

### `src/main/resources/application.properties`

```properties
spring.application.name=ms-usuario
server.port=8083

# Base de datos (MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/bookpoint_usuarios
spring.datasource.username=root
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### `src/test/resources/application.properties` (H2 en memoria)

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## 🌐 Acceso vía API Gateway

En producción no se accede directo al puerto `8083`, sino a través del gateway en el puerto `8080`:

```
GET http://localhost:8080/api/v1/usuarios
```

Ruta configurada en el gateway:

```yaml
- id: ms-usuario
  uri: http://localhost:8083
  predicates:
    - Path=/api/v1/usuarios,/api/v1/usuarios/**
```

---

## 📡 Endpoints

Base: `/api/v1/usuarios`

| Método | Endpoint                                | Descripción                          | Código éxito  |
| ------ | --------------------------------------- | ------------------------------------ | ------------- |
| `GET`  | `/api/v1/usuarios`                      | Listar todos los usuarios            | `200 OK`      |
| `GET`  | `/api/v1/usuarios/{id}`                 | Obtener un usuario por su ID         | `200 OK`      |
| `GET`  | `/api/v1/usuarios/correo/{emailCliente}`| Obtener un usuario por su correo     | `200 OK`      |
| `POST` | `/api/v1/usuarios`                      | Crear un nuevo usuario               | `201 Created` |

### JSON de entrada para `POST`

```json
{
  "nombre": "Filomeno",
  "apellido": "Rodriguez",
  "emailCliente": "f.rodriguez@gmail.com",
  "password": "12345",
  "rol": "USUARIO"
}
```

### Ejemplo de respuesta (HATEOAS)

`GET /api/v1/usuarios/1`

```json
{
  "id": 1,
  "nombre": "Filomeno",
  "apellido": "Rodriguez",
  "emailCliente": "f.rodriguez@gmail.com",
  "rol": "USUARIO",
  "_links": {
    "self": { "href": "http://localhost:8083/api/v1/usuarios/1" },
    "usuarios": { "href": "http://localhost:8083/api/v1/usuarios" }
  }
}
```

> 🔒 El campo `password` **no se incluye** en las respuestas; solo se recibe al crear o actualizar un usuario.

---

## 🗂️ Modelo de datos — `Usuario`

| Campo          | Tipo     | Descripción                              |
| -------------- | -------- | ---------------------------------------- |
| `id`           | `Long`   | Identificador único (PK)                 |
| `nombre`       | `String` | Nombre del usuario                       |
| `apellido`     | `String` | Apellido del usuario                     |
| `emailCliente` | `String` | Correo del usuario (único)               |
| `password`     | `String` | Contraseña (no se expone en respuestas)  |
| `rol`          | `String` | Rol del usuario (`USUARIO`, `ADMIN`, …)  |

---

## 🧪 Tests

- **Pruebas unitarias:** `@ExtendWith(MockitoExtension.class)` con `@Mock` / `@InjectMocks`.
- **Pruebas de integración:** `@SpringBootTest` + `@AutoConfigureMockMvc` + `@ActiveProfiles("test")`.
- **Cobertura:** ejecutar `./mvnw test` y revisar el reporte en `target/site/jacoco/index.html`.

---

## 📁 Estructura del proyecto

```
ms-usuario/
├── src/
│   ├── main/
│   │   ├── java/com/bookpoint/usuario/
│   │   │   ├── controller/
│   │   │   │   └── UsuarioController.java
│   │   │   ├── model/
│   │   │   │   └── Usuario.java
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── UsuarioApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/bookpoint/usuario/
│       └── resources/
│           └── application.properties
└── pom.xml
```

---

## 👤 Autor

Proyecto **BookPoint** — Microservicio de Usuario.
